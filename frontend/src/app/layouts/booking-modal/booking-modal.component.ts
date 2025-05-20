import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Location, NgClass, NgFor, NgIf} from '@angular/common';
import {Seat} from '../../core/models/seats.model';
import {Movie} from '../../core/models/movies.model';
import {LIST_TIME_SELECT} from '../../constants/common.constants';
import {NgSelectComponent} from '@ng-select/ng-select';
import {Shows} from '../../core/models/shows.model';
import {FormsModule} from '@angular/forms';
import {ShowsService} from '../../core/services/shows.service';
import {BaseFilterRequest} from '../../core/models/request.model';

@Component({
  selector: 'app-booking-modal',
  standalone: true,
  imports: [
    NgFor,
    NgIf,
    NgClass,
    NgSelectComponent,
    FormsModule
  ],
  templateUrl: './booking-modal.component.html',
  styleUrl: './booking-modal.component.scss'
})
export class BookingModalComponent implements OnInit {
  @Input() movie!: Movie;
  movieShows: Shows[] = [];
  selectedShowId: number = 0;
  selectedSeats: number[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private location: Location,
    private showService: ShowsService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss(false);
    });
  }

  ngOnInit(): void {
    if (this.movie && this.movie.id) {
      this.searchMovieShows();
    }
  }

  seats: Seat[] = [
    { id: 1, seat_number: 1, seat_row: 1, code: 'A1', status: 'ACTIVE' },
    { id: 2, seat_number: 2, seat_row: 1, code: 'A2', status: 'MAINTENANCE' },
    { id: 3, seat_number: 3, seat_row: 1, code: 'A3', status: 'ACTIVE' },
    { id: 4, seat_number: 4, seat_row: 1, code: 'A4', status: 'ACTIVE' },
    { id: 5, seat_number: 5, seat_row: 1, code: 'A5', status: 'ACTIVE' },
    { id: 6, seat_number: 1, seat_row: 2, code: 'B1', status: 'ACTIVE' },
    { id: 7, seat_number: 2, seat_row: 2, code: 'B2', status: 'ACTIVE' },
    { id: 8, seat_number: 3, seat_row: 2, code: 'B3', status: 'ACTIVE' },
    { id: 9, seat_number: 4, seat_row: 2, code: 'B4', status: 'ACTIVE' },
    { id: 10, seat_number: 5, seat_row: 2, code: 'B5', status: 'ACTIVE' },
    { id: 11, seat_number: 1, seat_row: 3, code: 'C1', status: 'ACTIVE' },
    { id: 12, seat_number: 2, seat_row: 3, code: 'C2', status: 'ACTIVE' },
    { id: 13, seat_number: 3, seat_row: 3, code: 'C3', status: 'ACTIVE' },
    { id: 14, seat_number: 4, seat_row: 3, code: 'C4', status: 'ACTIVE' },
    { id: 15, seat_number: 5, seat_row: 3, code: 'C5', status: 'ACTIVE' },
    { id: 16, seat_number: 1, seat_row: 4, code: 'D1', status: 'ACTIVE' },
    { id: 17, seat_number: 2, seat_row: 4, code: 'D2', status: 'ACTIVE' },
    { id: 18, seat_number: 3, seat_row: 4, code: 'D3', status: 'BOOKED' },
    // ...
  ];

  toggleSeatSelection(seat: Seat) {
    if (seat.status !== 'ACTIVE')
      return;

    const index = this.selectedSeats.indexOf(seat.id);

    if (index > -1) {
      this.selectedSeats.splice(index, 1);
    } else {
      this.selectedSeats.push(seat.id);
    }

    console.log(this.selectedSeats)
  }

  isSelected(seatId: number): boolean {
    return this.selectedSeats.includes(seatId);
  }

  searchMovieShows() {
    const searchRequest: BaseFilterRequest = {
      page: 0,
      size: 100
    }

    this.showService.getShowsByMovieId(this.movie.id, searchRequest).subscribe(response => {
      this.movieShows = response.result || [];
      this.selectedShowId = this.movieShows[0].id;
    })
  }

  changeShows() {

  }

  get rows(): number[] {
    return [...new Set(this.seats.map(s => s.seat_row))].sort();
  }

  get seatsByRow(): { [key: number]: Seat[] } {
    const map: { [key: number]: Seat[] } = {};
    for (let row of this.rows) {
      map[row] = this.seats
        .filter(s => s.seat_row === row)
        .sort((a, b) => a.seat_number - b.seat_number);
    }
    return map;
  }

  dismiss() {
    this.activeModal.dismiss(false);
  }

  protected readonly LIST_TIME_SELECT = LIST_TIME_SELECT;
}
