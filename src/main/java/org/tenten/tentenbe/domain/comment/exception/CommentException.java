package org.tenten.tentenbe.domain.comment.exception;

import org.springframework.http.HttpStatus;
import org.tenten.tentenbe.global.exception.GlobalException;

public class CommentException extends GlobalException {
    public CommentException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
