package org.tenten.tentenbe.domain.trip.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class TripLikedItemException extends GlobalException {
    public TripLikedItemException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
