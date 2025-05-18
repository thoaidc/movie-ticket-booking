package vn.ptit.moviebooking.seatavailability.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ptit.moviebooking.seatavailability.dto.response.BaseResponseDTO;

@FeignClient(name = "movie-service")
public interface MovieServiceClient {

    @GetMapping("/api/seats/by-all-shows")
    BaseResponseDTO getAllSeatsByShows();
}
