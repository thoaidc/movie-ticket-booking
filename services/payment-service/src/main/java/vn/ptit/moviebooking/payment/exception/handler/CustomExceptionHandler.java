package vn.ptit.moviebooking.payment.exception.handler;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.ptit.moviebooking.payment.constants.HttpStatusConstants;
import vn.ptit.moviebooking.payment.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.payment.exception.BaseBadRequestAlertException;
import vn.ptit.moviebooking.payment.exception.BaseBadRequestException;
import vn.ptit.moviebooking.payment.exception.BaseException;

import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    public CustomExceptionHandler() {
        log.debug("ResponseEntityExceptionHandler 'CustomExceptionHandler' is configured for handle exception");
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(@Nullable HttpRequestMethodNotSupportedException e,
                                                                      @Nullable HttpHeaders headers,
                                                                      @Nullable HttpStatusCode status,
                                                                      @Nullable WebRequest request) {
        log.error("Handle method not allow exception. {}", Objects.nonNull(e) ? e.getMessage() : "");

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.METHOD_NOT_ALLOWED)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message("Method not allow")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = "Invalid request data"; // Default message

        if (Objects.nonNull(fieldError))
            message = fieldError.getDefaultMessage(); // If the field with an error includes a custom message key

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message(message)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BaseBadRequestException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseBadRequestException(BaseBadRequestException exception) {
        log.error("[{}] Handle bad request exception: {}", exception.getEntityName(), exception.getMessage());

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BaseBadRequestAlertException.class })
    public ResponseEntity<Object> handleBaseBadRequestAlertException(BaseBadRequestAlertException e) {
        log.error("[{}] Handle bad request alert exception: {}", e.getEntityName(), e.getMessage());

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.INTERNAL_SERVER_ERROR)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseException(BaseException exception) {
        log.error("[{}] Handle exception: {}", exception.getEntityName(), exception.getMessage(), exception.getError());

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<BaseResponseDTO> handleException(Exception exception) {
        log.error("Handle unexpected exception", exception);

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(HttpStatusConstants.INTERNAL_SERVER_ERROR)
                .success(HttpStatusConstants.STATUS.FAILED)
                .message("Uncertain error")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
