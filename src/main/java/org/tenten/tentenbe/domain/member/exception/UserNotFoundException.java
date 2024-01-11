package org.tenten.tentenbe.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
