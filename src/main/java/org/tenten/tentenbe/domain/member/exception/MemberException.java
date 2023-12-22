package org.tenten.tentenbe.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class MemberException extends GlobalException {
    public MemberException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
