package vn.ptit.moviebooking.seatavailability.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ptit.moviebooking.seatavailability.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.seatavailability.service.CheckSeatAvailabilityService;

@RestController
@RequestMapping("/api/seats")
public class SeatShowResource {

    private final CheckSeatAvailabilityService checkSeatAvailabilityService;

    public SeatShowResource(CheckSeatAvailabilityService checkSeatAvailabilityService) {
        this.checkSeatAvailabilityService = checkSeatAvailabilityService;
    }

    @PostMapping("/init")
    public ResponseEntity<?> initSeatsOfShows() {
        return checkSeatAvailabilityService.initSeatsOfShow();
    }

    @GetMapping("/by-show/{showId}")
    public BaseResponseDTO getAllSeatsOfShow(@PathVariable Integer showId) {
        return checkSeatAvailabilityService.getAllSeatsOfShow(showId);
    }
}
