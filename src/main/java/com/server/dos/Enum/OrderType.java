package com.server.dos.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    WORKING("진행중"), COMPLETE("완료");

    private final String title;
}
