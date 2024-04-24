package edu.concordia.auth.exception;

import edu.concordia.shared.error.AppException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {

    private static HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public UserNotFoundException(String message) {
        super(message, httpStatus);
    }
}
