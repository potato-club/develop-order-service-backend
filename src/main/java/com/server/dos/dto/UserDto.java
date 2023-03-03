package com.server.dos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String nickname;
    private String email;
    private String picture;
}