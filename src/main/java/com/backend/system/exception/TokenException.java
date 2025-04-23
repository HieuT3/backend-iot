package com.backend.system.exception;

public class TokenException extends AppException{
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
