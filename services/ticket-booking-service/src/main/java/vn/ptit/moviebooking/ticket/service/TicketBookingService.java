package vn.ptit.moviebooking.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.ptit.moviebooking.ticket.constants.TicketBookingConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.request.CheckSeatAvailabilityRequest;
import vn.ptit.moviebooking.ticket.dto.request.CheckSeatAvailabilityRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ConfirmSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.PaymentRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.RefundRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ReserveSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequest;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.VerifyCustomerRequestCommand;
import vn.ptit.moviebooking.ticket.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.ticket.dto.response.BaseResponseDTO;
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
        booking.setStatus(TicketBookingConstants.BookingStatus.PENDING);
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
    public ValidateMovieRequestCommand createValidateMovieCommand(BookingRequest bookingRequest) {
        Booking booking = createBooking(bookingRequest);
        ValidateMovieRequestCommand validateMovieCommand = new ValidateMovieRequestCommand();
        ValidateMovieRequest validateMovieRequest = new ValidateMovieRequest();
        validateMovieCommand.setSagaId(booking.getId());
        validateMovieRequest.setShowId(booking.getShowId());
        validateMovieRequest.setSeatIds(bookingRequest.getSeatIds());
        validateMovieRequest.setTotalAmount(booking.getTotalAmount());
        validateMovieCommand.setValidateMovieRequest(validateMovieRequest);

        return validateMovieCommand;
    }

    public CheckSeatAvailabilityRequestCommand createCheckSeatsAvailabilityCommand(BaseCommandReplyMessage request) {
        CheckSeatAvailabilityRequestCommand command = new CheckSeatAvailabilityRequestCommand();
        CheckSeatAvailabilityRequest checkSeatAvailabilityRequest = new CheckSeatAvailabilityRequest();
        checkSeatAvailabilityRequest.setShowId(1);
        checkSeatAvailabilityRequest.setSeatIds(List.of());
        command.setSagaId(request.getSagaId());
        command.setCheckSeatAvailabilityRequest(checkSeatAvailabilityRequest);

        return command;
    }

    public ConfirmSeatsRequestCommand createConfirmBookingSeatsCommand(BaseCommandReplyMessage request) {
        ConfirmSeatsRequestCommand command = new ConfirmSeatsRequestCommand();

        return command;
    }

    public ReserveSeatsRequestCommand createReserveBookingSeatsCommand(BaseCommandReplyMessage request) {
        ReserveSeatsRequestCommand command = new ReserveSeatsRequestCommand();

        return command;
    }

    public VerifyCustomerRequestCommand createVerifyCustomerCommand(BaseCommandReplyMessage request) {
        VerifyCustomerRequestCommand command = new VerifyCustomerRequestCommand();

        return command;
    }

    public PaymentRequestCommand createPaymentProcessCommand(BaseCommandReplyMessage request) {
        PaymentRequestCommand command = new PaymentRequestCommand();

        return command;
    }

    public RefundRequestCommand createRefundCommand(BaseCommandReplyMessage request) {
        RefundRequestCommand command = new RefundRequestCommand();

        return command;
    }

    public void approveBooking(BaseCommandReplyMessage request) {

    }

    public void cancelBooking(BaseCommandReplyMessage request) {
        Optional<Booking> bookingOptional = ticketBookingRepository.findById(request.getSagaId());

        if (bookingOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Booking not exists, cannot update status");
        }

        Booking booking = bookingOptional.get();
        booking.setStatus(TicketBookingConstants.BookingStatus.CANCELLED);
        ticketBookingRepository.save(booking);
    }
}
