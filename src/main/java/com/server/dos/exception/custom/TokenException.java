package com.server.dos.exception.custom;

import com.server.dos.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String errorMessage;

    public TokenException(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
