package com.backend.system.exception;

public class TokenExpiredException extends TokenException{
    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
