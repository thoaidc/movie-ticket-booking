package vn.ptit.moviebooking.movie.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ptit.moviebooking.movie.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.movie.service.MovieService;

@RestController
@RequestMapping("/api/seats")
public class SeatResource {

    private final MovieService movieService;

    public SeatResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{roomId}")
    public BaseResponseDTO getAllSeatsByRoomId(@PathVariable Integer roomId) {
        return movieService.getAllSeatsByRoomId(roomId);
    }
}
