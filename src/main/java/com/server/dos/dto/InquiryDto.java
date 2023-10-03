package com.server.dos.dto;

import com.server.dos.Enum.InquiryResponse;
import lombok.Getter;

@Getter
public class InquiryDto {
    private String message;
    private String requestKey;
    private String responseKey;
    public InquiryDto(InquiryResponse response) {
        this.message = response.getMessage();
        this.requestKey = response.getKey();
        this.responseKey = response.getCall();
    }
}
