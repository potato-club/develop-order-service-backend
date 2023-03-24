package com.server.dos.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.server.dos.Enum.OrderType.*;

@Getter
@RequiredArgsConstructor
public enum OrderState {
    START("작업 시작", WORKING), DESIGN("디자인 회의", WORKING), PUBLISH("퍼블리싱", WORKING),
    IMPLEMENT("기능 구현", WORKING), FINAL("최종 수정", WORKING), COMPLETED("완료", COMPLETE);
    private final String detail;
    private final OrderType type;
}
