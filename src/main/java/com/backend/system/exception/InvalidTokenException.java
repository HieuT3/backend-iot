package com.backend.system.exception;

public class InvalidTokenException extends TokenException{
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
