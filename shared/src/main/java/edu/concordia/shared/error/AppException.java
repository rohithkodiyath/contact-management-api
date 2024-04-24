package edu.concordia.shared.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{

        private String message;

        private HttpStatus httpStatus;

        public AppException(String message, HttpStatus httpStatus){
            super(message);
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public String getMessage(){
            return message;
        }
}
