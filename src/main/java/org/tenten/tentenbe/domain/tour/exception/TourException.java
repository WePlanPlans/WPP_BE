package org.tenten.tentenbe.domain.tour.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class TourException extends GlobalException {
    public TourException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
