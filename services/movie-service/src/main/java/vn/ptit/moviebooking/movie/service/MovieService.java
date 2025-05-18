package vn.ptit.moviebooking.movie.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.movie.dto.request.BaseRequestDTO;
import vn.ptit.moviebooking.movie.dto.request.ValidateMovieRequest;
import vn.ptit.moviebooking.movie.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.movie.dto.response.ShowSeatResponse;
import vn.ptit.moviebooking.movie.entity.Movie;
import vn.ptit.moviebooking.movie.entity.Show;
import vn.ptit.moviebooking.movie.repository.CinemaRepository;
import vn.ptit.moviebooking.movie.repository.CinemaRoomRepository;
import vn.ptit.moviebooking.movie.repository.MovieRepository;
import vn.ptit.moviebooking.movie.repository.SeatRepository;
import vn.ptit.moviebooking.movie.repository.ShowRepository;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public MovieService(MovieRepository movieRepository,
                        CinemaRepository cinemaRepository,
                        CinemaRoomRepository cinemaRoomRepository,
                        ShowRepository showRepository,
                        SeatRepository seatRepository) {
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    public BaseResponseDTO validateMovieInfo(ValidateMovieRequest request) {
        return BaseResponseDTO.builder().ok();
    }

    public BaseResponseDTO getAllMovieWithPaging(BaseRequestDTO request) {
        Page<Movie> moviesPaged = movieRepository.findAllWithPaging(
                request.getKeywordSearch(),
                request.getFromDateSearch(),
                request.getToDateSearch(),
                request.getPageable()
        );

        return BaseResponseDTO.builder().total(moviesPaged.getTotalElements()).ok(moviesPaged.getContent());
    }

    public BaseResponseDTO getMovieDetail(Integer movieId) {
        return BaseResponseDTO.builder().ok(movieRepository.findById(movieId));
    }

    public BaseResponseDTO getAllCinemas() {
        return BaseResponseDTO.builder().ok(cinemaRepository.findAll());
    }

    public BaseResponseDTO getAllCinemaRooms(Integer cinemaId) {
        return BaseResponseDTO.builder().ok(cinemaRoomRepository.findAllByCinemaId(cinemaId));
    }

    public BaseResponseDTO getAllShowsByMovieId(Integer movieId) {
        return BaseResponseDTO.builder().ok(showRepository.findAllByMovieId(movieId));
    }

    public BaseResponseDTO getAllSeatsByShowId(Integer showId) {
        return BaseResponseDTO.builder().ok(seatRepository.findAllByShowId(showId));
    }

    public BaseResponseDTO getAllSeatsGroupedByShow() {
        List<Show> shows = showRepository.findAll();

        List<ShowSeatResponse> result = shows.stream().map(show -> {
            List<Integer> seats = seatRepository.findAllIdByCinemaRoomId(show.getCinemaRoomId());
            return new ShowSeatResponse(show.getId(), seats);
        }).toList();

        return BaseResponseDTO.builder().ok(result);
    }
}
