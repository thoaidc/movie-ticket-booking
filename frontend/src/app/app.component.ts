import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingBarModule} from '@ngx-loading-bar/core';
import {Subscription} from 'rxjs';
import {WebsocketService} from './core/services/websocket.service';
import {IMessage} from '@stomp/stompjs';
import {BaseResponse} from './core/models/response.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingBarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  private stateSubscription: Subscription | null = null;
  private topicSubscription: Subscription | null = null;

  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.stateSubscription = this.websocketService.onState().subscribe();
    this.websocketService.connect();

    this.topicSubscription = this.websocketService.subscribeToTopic(`/topics/bookings`).subscribe({
      next: (message: IMessage) => this.handleWebSocketMessage(message),
      error: () => {}
    });
  }

  private handleWebSocketMessage(message: IMessage) {
    const response: BaseResponse<any> = JSON.parse(message.body) as BaseResponse<any>;
    console.log(response);
  }

  ngOnDestroy(): void {
    if (this.stateSubscription) {
      this.websocketService.unsubscribeFromTopic(`/topics/bookings`);
      this.stateSubscription.unsubscribe();
    }

    this.websocketService.disconnect();
  }
}
