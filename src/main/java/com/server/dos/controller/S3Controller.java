package com.server.dos.controller;

import com.server.dos.service.S3Service;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "파일 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class S3Controller {
    private final S3Service s3Service;

    @Operation(summary = "파일 다운로드 API")
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable(value = "fileName") String fileName) throws IOException {
        return s3Service.download(fileName);
    }
}
