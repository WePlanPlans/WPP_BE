package org.tenten.tentenbe.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class ReviewException extends GlobalException {
    public ReviewException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
