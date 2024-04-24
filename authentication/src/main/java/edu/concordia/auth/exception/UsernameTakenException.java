package edu.concordia.auth.exception;

import edu.concordia.shared.error.AppException;
import org.springframework.http.HttpStatus;

public class UsernameTakenException extends AppException {

    private static HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

    public UsernameTakenException(String message) {
        super(message, httpStatus);
    }
}
