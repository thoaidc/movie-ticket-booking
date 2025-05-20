import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {ApplicationConfigService} from '../config/application-config.service';
import {
  API_CREATE_BOOKING,
  API_PAYMENT,
  API_VERIFY_CUSTOMER_INFO
} from '../../constants/api.constants';
import {Booking, Customer, Payment} from '../models/bookings.model';
import {Observable} from 'rxjs';
import {BaseResponse} from '../models/response.model';

@Injectable({
  providedIn: 'root',
})
export class BookingService {

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private createBookingApi = this.applicationConfigService.getEndpointFor(API_CREATE_BOOKING);
  private verifyCustomerApi = this.applicationConfigService.getEndpointFor(API_VERIFY_CUSTOMER_INFO);
  private paymentApi = this.applicationConfigService.getEndpointFor(API_PAYMENT);

  createBookingRequest(bookingRequest: Booking): Observable<BaseResponse<any>> {
    return this.http.post<BaseResponse<any>>(this.createBookingApi, bookingRequest);
  }

  verifyCustomerInfo(customer: Customer): Observable<BaseResponse<any>> {
    return this.http.post<BaseResponse<any>>(this.verifyCustomerApi, customer);
  }

  payment(payment: Payment): Observable<BaseResponse<any>> {
    return this.http.post<BaseResponse<any>>(this.paymentApi, payment);
  }
}
