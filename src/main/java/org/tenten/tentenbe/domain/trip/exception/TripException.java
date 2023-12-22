package org.tenten.tentenbe.domain.trip.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class TripException extends GlobalException {
    public TripException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
