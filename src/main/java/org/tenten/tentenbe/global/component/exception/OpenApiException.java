package org.tenten.tentenbe.global.component.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class OpenApiException extends GlobalException {
    public OpenApiException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
