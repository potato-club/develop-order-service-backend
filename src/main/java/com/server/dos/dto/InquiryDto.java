package com.server.dos.dto;

import com.server.dos.Enum.InquiryResponse;
import lombok.Getter;

@Getter
public class InquiryDto {
    private String requestKey;
    private String message;
    private String responseKey;
    public InquiryDto(InquiryResponse response) {
        this.requestKey = response.getKey();
        this.message = response.getMessage();
        this.responseKey = response.getCall();
    }
}
