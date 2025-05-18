package vn.ptit.moviebooking.seatavailability.resource;

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
    public BaseResponseDTO initSeatsOfShows() {
        return checkSeatAvailabilityService.initSeatsOfShow();
    }
}
