package com.server.dos.controller;

import com.server.dos.dto.InquiryDto;
import com.server.dos.service.InquiryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "문의 API")
@RequiredArgsConstructor
@RequestMapping("/inquiry")
@RestController
public class InquiryController {
    private final InquiryService inquiryService;

    @Operation(summary = "문의 요청 자동응답 API")
    @GetMapping("")
    public List<InquiryDto> handleInquiry(@RequestParam(value = "option", defaultValue = "0") String option) {
        return inquiryService.getResponse(option);
    }
}
