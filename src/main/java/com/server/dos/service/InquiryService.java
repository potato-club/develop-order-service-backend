package com.server.dos.service;

import com.server.dos.Enum.InquiryResponse;
import com.server.dos.exception.custom.InquiryException;
import com.server.dos.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InquiryService {
    public String getResponse(String option) {
        InquiryResponse response = InquiryResponse.findWithKey(option);
        if(response == null) throw new InquiryException(ErrorCode.BAD_REQUEST, "잘못된 option 값입니다.");
        return response.getMessage();
    }
}
