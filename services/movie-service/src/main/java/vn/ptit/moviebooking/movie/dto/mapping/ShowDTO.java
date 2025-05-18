package vn.ptit.moviebooking.movie.dto.mapping;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public interface ShowDTO {

    String getId();
    String getMovie();
    String getRoom();
    Float getTicketPrice();
    String getStatus();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "UTC")
    Instant getStartTime();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "UTC")
    Instant getEndTime();
}
