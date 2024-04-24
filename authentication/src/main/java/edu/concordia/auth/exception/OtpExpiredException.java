package edu.concordia.auth.exception;

import edu.concordia.shared.error.AppException;
import org.springframework.http.HttpStatus;

public class OtpExpiredException extends AppException {

    private static HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

    public OtpExpiredException(String message) {
        super(message, httpStatus);
    }
}
