import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Location, NgClass, NgFor, NgIf} from '@angular/common';
import {Seat, SeatShow, SeatStatus} from '../../core/models/seats.model';
import {Movie} from '../../core/models/movies.model';
import {NgSelectComponent} from '@ng-select/ng-select';
import {Shows} from '../../core/models/shows.model';
import {FormsModule} from '@angular/forms';
import {ShowsService} from '../../core/services/shows.service';
import {BaseFilterRequest} from '../../core/models/request.model';
import {SeatService} from '../../core/services/seats.service';

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
  selectedShowInfo: Shows | undefined;
  selectedSeats: number[] = [];
  seats: Seat[] = [];
  seatsShow: SeatShow[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private location: Location,
    private showService: ShowsService,
    private seatService: SeatService,
    private cdr: ChangeDetectorRef
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

  toggleSeatSelection(seat: SeatShow) {
    if (seat.status !== SeatStatus.AVAILABLE)
      return;

    const index = this.selectedSeats.indexOf(seat.id);

    if (index > -1) {
      this.selectedSeats.splice(index, 1);
    } else {
      this.selectedSeats.push(seat.id);
    }
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
      this.changeShows();
    })
  }

  changeShows() {
    this.selectedShowInfo = this.movieShows.find(s => s.id === this.selectedShowId);

    this.seatService.getSeatsShowByShowId(this.selectedShowId).subscribe(response => {
      this.seatsShow = response.result || [];

      this.seatService.getSeatsByShowId(this.selectedShowId).subscribe(response => {
        this.seats = response.result || [];

        this.seatsShow = this.seatsShow.map(seatShow => {
          const seat = this.seats.find(s => s.id === seatShow.seatId);

          return {
            ...seatShow,
            seatNumber: seat?.seatNumber,
            seatRow: seat?.seatRow,
            code: seat?.code
          };
        });

        this.cdr.detectChanges();
      });
    });
  }

  get rows(): number[] {
    return [...new Set(this.seatsShow.map(s => s.seatRow || 0))].sort();
  }

  get seatsByRow(): { [key: number]: SeatShow[] } {
    const map: { [key: number]: SeatShow[] } = {};

    for (let row of this.rows) {
      map[row] = this.seatsShow.filter(s => s.seatRow === row)
        .sort((a, b) => ((a.seatNumber || 0) - (b.seatNumber || 0)));
    }

    return map;
  }

  dismiss() {
    this.activeModal.dismiss(false);
  }
}
