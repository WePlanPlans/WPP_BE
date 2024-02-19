package org.tenten.tentenbe.domain.admin.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class AdminException extends GlobalException {
    public AdminException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
