import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingBarModule} from '@ngx-loading-bar/core';
import {Subscription} from 'rxjs';
import {WebsocketService} from './core/services/websocket.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingBarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  private stateSubscription: Subscription | null = null;

  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.stateSubscription = this.websocketService.onState().subscribe();
    this.websocketService.connect();
  }

  ngOnDestroy(): void {
    if (this.stateSubscription) {
      this.stateSubscription.unsubscribe();
    }

    this.websocketService.disconnect();
  }
}
