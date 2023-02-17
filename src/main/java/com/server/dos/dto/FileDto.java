package com.server.dos.dto;

import lombok.Data;

@Data
public class FileDto {
    private Long id;
    private String fileName;
    private String s3Url;
}
