package edu.concordia.auth.exception;

import edu.concordia.shared.error.AppException;
import org.springframework.http.HttpStatus;

public class InvalidOtpException extends AppException {

    private static HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

    public InvalidOtpException(String message) {
        super(message, httpStatus);
    }
}
