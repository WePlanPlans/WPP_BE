package org.tenten.tentenbe.domain.tour.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class TourNotFoundException extends GlobalException {
    public TourNotFoundException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
