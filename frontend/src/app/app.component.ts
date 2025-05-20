import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingBarModule} from '@ngx-loading-bar/core';
import {Subscription} from 'rxjs';
import {WebsocketService} from './core/services/websocket.service';
import {IMessage} from '@stomp/stompjs';
import {BaseResponse} from './core/models/response.model';
import {DateFilterComponent} from './shared/components/date-filter/date-filter.component';
import {NgbDatepickerModule, NgbModal, NgbModalRef, NgbPagination, NgbTooltip} from '@ng-bootstrap/ng-bootstrap';
import {NgSelectModule} from '@ng-select/ng-select';
import {SafeHtmlPipe} from './shared/pipes/safe-html.pipe';
import {DatePipe, DecimalPipe, NgClass, NgFor, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import dayjs from 'dayjs/esm';
import {UtilsService} from './shared/utils/utils.service';
import {ToastrService} from 'ngx-toastr';
import {ICON_RELOAD, ICON_SEARCH} from './shared/utils/icon';
import {PAGINATION_PAGE_SIZE} from './constants/common.constants';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    LoadingBarModule,
    DateFilterComponent,
    NgbDatepickerModule,
    NgSelectModule,
    NgbPagination,
    SafeHtmlPipe,
    NgIf,
    NgFor,
    FormsModule,
    NgClass,
    DatePipe,
    NgbTooltip,
    DecimalPipe
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  private stateSubscription: Subscription | null = null;
  private topicSubscription: Subscription | null = null;
  private modalRef: NgbModalRef | undefined;
  periods: number = 5;  // This year
  totalItems: number = 0;
  firmsFilter = {
    page: 1,
    size: 10,
    keyword: '',
    fromDate: dayjs().startOf('day'),
    toDate: dayjs().endOf('day')
  }

  isLoading = false;

  constructor(private websocketService: WebsocketService,
              protected modalService: NgbModal,
              protected utilsService: UtilsService,
              private toast: ToastrService) {}

  ngOnInit(): void {
    this.onSearch();
    this.stateSubscription = this.websocketService.onState().subscribe();
    this.websocketService.connect();

    this.topicSubscription = this.websocketService.subscribeToTopic(`/topics/bookings`).subscribe({
      next: (message: IMessage) => this.handleWebSocketMessage(message),
      error: (e) => console.log(e)
    });
  }

  private handleWebSocketMessage(message: IMessage) {
    const response: BaseResponse<any> = JSON.parse(message.body) as BaseResponse<any>;
    console.log(response);
  }

  onTimeChange(even: any) {
    this.firmsFilter.fromDate = even.fromDate;
    this.firmsFilter.toDate = even.toDate;
    this.firmsFilter.page = 1;
    this.periods = even.periods;
    this.onSearch();
  }

  onSearch() {

  }

  onReload() {
    this.periods = 5;
    this.firmsFilter = {
      page: 1,
      size: 10,
      keyword: '',
      fromDate: dayjs().startOf('day'),
      toDate: dayjs().endOf('day')
    }

    this.onSearch();
  }

  loadMore($event: any) {
    this.firmsFilter.page = $event;
    this.onSearch();
  }

  ngOnDestroy(): void {
    if (this.stateSubscription) {
      this.websocketService.unsubscribeFromTopic(`/topics/bookings`);
      this.stateSubscription.unsubscribe();
    }

    this.websocketService.disconnect();
  }

  protected readonly ICON_RELOAD = ICON_RELOAD;
  protected readonly ICON_SEARCH = ICON_SEARCH;
  protected readonly PAGINATION_PAGE_SIZE = PAGINATION_PAGE_SIZE;
}
