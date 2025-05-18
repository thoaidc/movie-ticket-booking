package vn.ptit.moviebooking.movie.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ptit.moviebooking.movie.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.movie.service.MovieService;

@RestController
@RequestMapping("/api/shows")
public class ShowResource {

    private final MovieService movieService;

    public ShowResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}")
    public BaseResponseDTO getAllShowsByMovieId(@PathVariable Integer movieId) {
        return movieService.getAllShowsByMovieId(movieId);
    }
}
