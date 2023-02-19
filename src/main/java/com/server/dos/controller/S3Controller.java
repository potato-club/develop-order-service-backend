package com.server.dos.controller;

import com.server.dos.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Service;
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable(value = "fileName") String fileName) throws IOException {
        return s3Service.download(fileName);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> delete(@PathVariable(value = "fileName") String fileName) {
        s3Service.delete(fileName);
        return ResponseEntity.ok("삭제 성공");
    }
}
