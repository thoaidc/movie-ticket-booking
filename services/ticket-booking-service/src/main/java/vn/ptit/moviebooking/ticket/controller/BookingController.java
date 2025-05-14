package vn.ptit.moviebooking.ticket.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.ticket.service.SagaCommandProducer;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final SagaCommandProducer sagaCommandProducer;

    public BookingController(SagaCommandProducer sagaCommandProducer) {
        this.sagaCommandProducer = sagaCommandProducer;
    }

    @PostMapping
    public BaseResponseDTO createBooking(@RequestBody BookingRequest request) {
        return sagaCommandProducer.createBookingSagaOrchestrator(request);
    }
}
