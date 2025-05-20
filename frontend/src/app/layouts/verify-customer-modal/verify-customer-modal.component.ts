import {Component, Input} from '@angular/core';
import {AlphanumericOnlyDirective} from "../../shared/directives/alphanumeric-only.directive";
import {FormsModule} from "@angular/forms";
import {Location, NgIf} from '@angular/common';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Customer} from '../../core/models/bookings.model';
import {BookingService} from '../../core/services/bookings.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-verify-customer-modal',
  standalone: true,
  imports: [
    AlphanumericOnlyDirective,
    FormsModule,
    NgIf
  ],
  templateUrl: './verify-customer-modal.component.html',
  styleUrl: './verify-customer-modal.component.scss'
})
export class VerifyCustomerModalComponent {
  customer: Customer = {
    fullname: '',
    email: '',
    phone: '',
    bookingId: 0
  }

  @Input() bookingId: number = 0;

  constructor(
    public activeModal: NgbActiveModal,
    private location: Location,
    private bookingService: BookingService,
    private toast: ToastrService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss(false);
    });
  }

  initialize() {
    this.customer.bookingId = this.bookingId;
  }

  confirm() {
    this.initialize();

    this.bookingService.verifyCustomerInfo(this.customer).subscribe(response => {
      if (response.status) {
        this.toast.success('Đang xác minh thông tin của bạn', 'Thông báo');
      } else {
        this.toast.error('Gửi thông tin xác minh thất bại', 'Thông báo');
      }

      this.activeModal.close(response.status);
    });
  }

  dismiss() {
    this.activeModal.dismiss(false);
  }
}
