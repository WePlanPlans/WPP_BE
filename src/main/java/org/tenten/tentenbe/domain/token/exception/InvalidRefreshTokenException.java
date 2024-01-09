package org.tenten.tentenbe.domain.token.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class InvalidRefreshTokenException extends GlobalException {
    public InvalidRefreshTokenException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
