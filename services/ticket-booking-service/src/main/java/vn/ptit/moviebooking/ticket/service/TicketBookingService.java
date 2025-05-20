package vn.ptit.moviebooking.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.ptit.moviebooking.ticket.constants.BookingConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.request.CheckSeatAvailabilityRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ConfirmSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.NotificationCommand;
import vn.ptit.moviebooking.ticket.dto.request.NotificationRequest;
import vn.ptit.moviebooking.ticket.dto.request.ReleasedSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequest;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequestCommand;
import vn.ptit.moviebooking.ticket.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.ticket.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.ticket.dto.response.CustomerDTO;
import vn.ptit.moviebooking.ticket.entity.Booking;
import vn.ptit.moviebooking.ticket.entity.BookingSeat;
import vn.ptit.moviebooking.ticket.exception.BaseBadRequestException;
import vn.ptit.moviebooking.ticket.repository.BookingSeatRepository;
import vn.ptit.moviebooking.ticket.repository.TicketBookingRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketBookingService {

    private final TicketBookingRepository ticketBookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final CustomerServiceClient customerServiceClient;
    private final ObjectMapper objectMapper;
    private static final String ENTITY_NAME = "TicketBookingService";

    public TicketBookingService(TicketBookingRepository ticketBookingRepository,
                                BookingSeatRepository bookingSeatRepository,
                                CustomerServiceClient customerServiceClient,
                                ObjectMapper objectMapper) {
        this.ticketBookingRepository = ticketBookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.customerServiceClient = customerServiceClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setShowId(bookingRequest.getShowId());
        booking.setTotalAmount(bookingRequest.getTotalAmount());
        booking.setStatus(BookingConstants.Status.PENDING);
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
        validateMovieRequest.setMovieId(bookingRequest.getMovieId());
        validateMovieRequest.setShowId(booking.getShowId());
        validateMovieCommand.setSagaId(booking.getId());
        validateMovieCommand.setValidateMovieRequest(validateMovieRequest);

        return validateMovieCommand;
    }

    public CheckSeatAvailabilityRequestCommand createCheckSeatsAvailabilityCommand(BaseCommandReplyMessage request) {
        CheckSeatAvailabilityRequestCommand command = new CheckSeatAvailabilityRequestCommand();
        command.setSeatIds(bookingSeatRepository.findAllSeatIdsByBookingId(request.getSagaId()));
        command.setSagaId(request.getSagaId());
        return command;
    }

    public ConfirmSeatsRequestCommand createConfirmBookingSeatsCommand(BaseCommandReplyMessage request) {
        ConfirmSeatsRequestCommand command = new ConfirmSeatsRequestCommand();
        command.setSeatIds(bookingSeatRepository.findAllSeatIdsByBookingId(request.getSagaId()));
        command.setSagaId(request.getSagaId());
        return command;
    }

    public ReleasedSeatsRequestCommand createReleasedBookingSeatsCommand(BaseCommandReplyMessage request) {
        ReleasedSeatsRequestCommand command = new ReleasedSeatsRequestCommand();
        command.setSeatIds(bookingSeatRepository.findAllSeatIdsByBookingId(request.getSagaId()));
        command.setSagaId(request.getSagaId());
        return command;
    }

    public void updateBookingCustomerInfo(BaseCommandReplyMessage request) {
        Integer customerId = (Integer) request.getResult();
        Optional<Booking> bookingOptional = ticketBookingRepository.findById(request.getSagaId());

        if (bookingOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Booking not exists, cannot update customer info");
        }

        Booking booking = bookingOptional.get();
        booking.setCustomerId(customerId);
        ticketBookingRepository.save(booking);
    }

    public void updateBookingStatus(Integer bookingId, String status) {
        Optional<Booking> bookingOptional = ticketBookingRepository.findById(bookingId);

        if (bookingOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Booking not exists, cannot update status");
        }

        Booking booking = bookingOptional.get();
        booking.setStatus(status);
        ticketBookingRepository.save(booking);
    }

    public NotificationCommand createNotificationCommand(BaseCommandReplyMessage request) {
        Integer customerId = ticketBookingRepository.findCustomerIdByBookingId(request.getSagaId());
        BaseResponseDTO responseDTO = customerServiceClient.getCustomerInfo(customerId);
        NotificationCommand command = new NotificationCommand();
        command.setSagaId(request.getSagaId());

        if (responseDTO.getStatus() && Objects.nonNull(responseDTO.getResult())) {
            try {
                CustomerDTO customerDTO = objectMapper.convertValue(responseDTO.getResult(), CustomerDTO.class);
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setSender("MOVIE-BOOKING-SYSTEM");
                notificationRequest.setReceiver(customerDTO.getEmail());
                notificationRequest.setTitle("Your ticket booking has completed successfully!");
                notificationRequest.setContent("Please double check the information on your ticket and make sure it is correct.");
                command.setNotificationRequest(notificationRequest);
            } catch (Exception ignored) {}
        }

        return command;
    }
}
