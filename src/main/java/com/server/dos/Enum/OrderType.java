package com.server.dos.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    READY("작업 대기"), WORKING("진행중"), COMPLETE("완료");

    private final String title;
}
