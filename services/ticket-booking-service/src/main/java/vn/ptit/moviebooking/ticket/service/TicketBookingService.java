package vn.ptit.moviebooking.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.moviebooking.ticket.constants.TicketBookingConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.entity.Booking;
import vn.ptit.moviebooking.ticket.entity.BookingSeat;
import vn.ptit.moviebooking.ticket.exception.BaseBadRequestException;
import vn.ptit.moviebooking.ticket.repository.BookingSeatRepository;
import vn.ptit.moviebooking.ticket.repository.TicketBookingRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketBookingService {

    private final TicketBookingRepository ticketBookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private static final String ENTITY_NAME = "TicketBookingService";

    public TicketBookingService(TicketBookingRepository ticketBookingRepository,
                                BookingSeatRepository bookingSeatRepository) {
        this.ticketBookingRepository = ticketBookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setShowId(bookingRequest.getShowId());
        booking.setTotalAmount(bookingRequest.getTotalAmount());
        booking.setStatus(TicketBookingConstants.BOOKING_STATUS.PENDING);
        booking.setCreateTime(Instant.now());
        ticketBookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Integer seatId : bookingRequest.getSeatIds()) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBookingId(booking.getId());
            bookingSeat.setSeatId(seatId);
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);

        return booking;
    }

    @Transactional
    public void updateBookingStatus(Integer bookingId, String status) {
        Optional<Booking> bookingOptional = ticketBookingRepository.findById(bookingId);

        if (bookingOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Booking not exists, cannot update status");
        }

        Booking booking = bookingOptional.get();
        booking.setStatus(status);
        ticketBookingRepository.save(booking);
    }
}
