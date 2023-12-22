package org.tenten.tentenbe.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class AuthException extends GlobalException {
    public AuthException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
