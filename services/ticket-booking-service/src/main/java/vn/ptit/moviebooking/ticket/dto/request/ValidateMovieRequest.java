package vn.ptit.moviebooking.ticket.dto.request;

public class ValidateMovieRequest {

    private Integer showId;
    private Integer movieId;

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
