package ac.at.fhcampuswien.authenticationms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Europe/Vienna"));

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiException> handleCustomerNotFoundException(CustomerNotFoundException customerNotFoundException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                customerNotFoundException.getMessage(),
                httpStatus,
                timestamp
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiException> handleInvalidTokenException(InvalidTokenException invalidTokenException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                invalidTokenException.getMessage(),
                httpStatus,
                timestamp
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }



}
