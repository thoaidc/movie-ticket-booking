package vn.ptit.moviebooking.seatavailability.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.moviebooking.seatavailability.constants.HttpStatusConstants;
import vn.ptit.moviebooking.seatavailability.constants.SeatsConstants;
import vn.ptit.moviebooking.seatavailability.dto.request.SeatsCommand;
import vn.ptit.moviebooking.seatavailability.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.seatavailability.dto.response.ShowSeatResponse;
import vn.ptit.moviebooking.seatavailability.entity.SeatShow;
import vn.ptit.moviebooking.seatavailability.repository.SeatShowRepository;

import java.util.List;
import java.util.Objects;

@Service
public class CheckSeatAvailabilityService {

    private final MovieServiceClient movieServiceClient;
    private final SeatShowRepository seatShowRepository;
    private final ObjectMapper objectMapper;

    public CheckSeatAvailabilityService(MovieServiceClient movieServiceClient,
                                        SeatShowRepository seatShowRepository,
                                        ObjectMapper objectMapper) {
        this.movieServiceClient = movieServiceClient;
        this.seatShowRepository = seatShowRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ResponseEntity<?> initSeatsOfShow() {
        BaseResponseDTO response = movieServiceClient.getAllSeatsByShows();

        if (!response.getStatus() || Objects.isNull(response.getResult())) {
            return ResponseEntity.internalServerError().build();
        }

        List<?> rawList = (List<?>) response.getResult();
        List<ShowSeatResponse> seats = rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, ShowSeatResponse.class))
                .toList();

        List<SeatShow> seatShows = seats.stream().flatMap(showSeatResponse -> showSeatResponse.getSeatIds()
            .stream().map(seatId -> {
                SeatShow seatShow = new SeatShow();
                seatShow.setSeatId(seatId);
                seatShow.setShowId(showSeatResponse.getShowId());
                seatShow.setStatus(SeatsConstants.Status.AVAILABLE);
                return seatShow;
            })
        )
        .toList();

        seatShowRepository.deleteAll();
        seatShowRepository.saveAll(seatShows);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public BaseResponseDTO checkSeatsAvailability(SeatsCommand command) {
        List<SeatShow> seatShows = seatShowRepository
                .findAllByIdInAndStatusForUpdate(command.getSeatIds(), SeatsConstants.Status.AVAILABLE);

        if (seatShows.size() == command.getSeatIds().size()) {
            seatShows.forEach(seat -> seat.setStatus(SeatsConstants.Status.RESERVED));
            seatShowRepository.saveAll(seatShows);
            return BaseResponseDTO.builder().ok();
        }

        return BaseResponseDTO.builder().code(HttpStatusConstants.BAD_REQUEST).success(false).build();
    }

    @Transactional
    public BaseResponseDTO confirmBookedSeats(SeatsCommand command) {
        List<SeatShow> seatShows = seatShowRepository.findAllById(command.getSeatIds());
        seatShows.forEach(seat -> seat.setStatus(SeatsConstants.Status.BOOKED));
        seatShowRepository.saveAll(seatShows);
        return BaseResponseDTO.builder().ok();
    }

    @Transactional
    public BaseResponseDTO releasedBookedSeats(SeatsCommand command) {
        List<SeatShow> seatShows = seatShowRepository.findAllById(command.getSeatIds());
        seatShows.forEach(seat -> seat.setStatus(SeatsConstants.Status.AVAILABLE));
        seatShowRepository.saveAll(seatShows);
        return BaseResponseDTO.builder().ok();
    }
}
