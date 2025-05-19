package vn.ptit.moviebooking.movie.dto.request;

public class ValidateMovieCommand {

    private int sagaId;
    private ValidateMovieRequest validateMovieRequest;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public ValidateMovieRequest getValidateMovieRequest() {
        return validateMovieRequest;
    }

    public void setValidateMovieRequest(ValidateMovieRequest validateMovieRequest) {
        this.validateMovieRequest = validateMovieRequest;
    }
}
