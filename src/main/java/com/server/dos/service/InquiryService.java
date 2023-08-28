package com.server.dos.service;

import com.server.dos.Enum.InquiryResponse;
import com.server.dos.dto.InquiryDto;
import com.server.dos.exception.custom.InquiryException;
import com.server.dos.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InquiryService {
    public List<InquiryDto> getResponse(String option) {
        List<InquiryResponse> response = InquiryResponse.findWithKey(option);
        if(response.isEmpty()) throw new InquiryException(ErrorCode.BAD_REQUEST, "잘못된 option 값입니다.");
        return response.stream().map(InquiryDto::new).collect(Collectors.toList());
    }
}
