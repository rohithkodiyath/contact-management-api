package edu.concordia.shared.errors;

import edu.concordia.shared.error.AppException;
import org.springframework.http.HttpStatus;

public class ContactNotFoundException extends AppException {

        public ContactNotFoundException(String uuid){
            super("Contact with uuid: " + uuid + " not found", HttpStatus.NOT_FOUND);
        }
}
