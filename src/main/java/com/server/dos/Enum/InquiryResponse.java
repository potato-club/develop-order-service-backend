package com.server.dos.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum InquiryResponse {
    RESPONSE_1("1", "고객님이 신청하신 발주신청을 토대로, 1. 디자인회의, " +
            "2. 퍼블리싱 작업, 3. 페이지 내 기능구현 작업, 4. 최종수정 작업을 거쳐 발주가 이루어지게 됩니다."),
    RESPONSE_2_1("2-1", "사이트 상단의 프로필을 통해 접근이 가능한 '마이페이지'의 '내 발주신청'에서, " +
            "내 발주신청을 취소할 수 있습니다."),
    RESPONSE_2_2("2-2", "사이트 상단의 프로필을 통해 접근이 가능한 '마이페이지'의 '내 발주신청'에서, " +
            "내 발주신청을 확인할 수 있습니다."),
    RESPONSE_2_3("2-3", "DOS 고객센터에 전화를 주시면, 간단한 정보 수정은 바로 처리할 수 있습니다."),
    RESPONSE_3_1("3-1", "사이트 상단의 프로필을 통해 접근이 가능한 '마이페이지'의 '내 발주현황'에서, " +
            "내 발주현황을 확인할 수 있습니다."),
    RESPONSE_3_2("3-2", "사이트 상단의 프로필을 통해 접근이 가능한 '마이페이지'의 '내 발주현황'에서, " +
            "내 발주현황을 취소할 수 있습니다. 하지만 발주 진행 도중의 취소 시, 발주신청의 요금은 돌려드리지 않습니다."),
    RESPONSE_3_3("3-3", "발주가 완료된 내 발주현황에 대해서만 별점 부여가 가능합니다. 별점을 지정하여 저장하시면, " +
            "다시는 해당 발주의 별점은 변경하실 수 없습니다."),
    RESPONSE_4("4", "사이트 상단의 프로필을 통해 접근이 가능한 '마이페이지'의 '회원탈퇴'에서, 회원탈퇴가 가능합니다. " +
            "회원탈퇴 시 '내 발주신청', '내 발주현황', '내 좋아요'의 정보들은 계속 남아있지만 더 이상 고객님의 계정과 연동되지 않습니다.");

    private final String key;
    private final String message;
    public static InquiryResponse findWithKey(String key) {
        return Arrays.stream(values()).filter(state -> Objects.equals(state.key, key)).findAny().orElse(null);
    }
}
