package vn.ptit.moviebooking.ticket.dto.request;

public class ValidateMovieRequestCommand extends BaseCommandDTO {

    private ValidateMovieRequest validateMovieRequest;

    public ValidateMovieRequest getValidateMovieRequest() {
        return validateMovieRequest;
    }

    public void setValidateMovieRequest(ValidateMovieRequest validateMovieRequest) {
        this.validateMovieRequest = validateMovieRequest;
    }
}
