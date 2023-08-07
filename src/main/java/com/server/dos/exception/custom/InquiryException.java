package com.server.dos.exception.custom;

import com.server.dos.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class InquiryException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String errorMessage;
    public InquiryException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
