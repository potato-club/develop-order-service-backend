package com.server.dos.dto;

import lombok.Data;

@Data
public class OrderDetailRequestDto {
    private Boolean database;
    private Boolean login;
    private Integer page;
    private Integer stateKey;
}
