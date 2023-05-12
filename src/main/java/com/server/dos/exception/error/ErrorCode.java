package com.server.dos.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// 커스텀 에러 코드 만들기
@Getter
@AllArgsConstructor
@ToString
public enum ErrorCode {
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    FORBIDDEN(403,HttpStatus.FORBIDDEN,"미승인되어 접근권한이 없습니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "페이지를 찾을 수 없습니다."),
    CONFLICT(409, HttpStatus.CONFLICT, "서버 상태와 충돌합니다."),
    EXPIRED_TOKEN(444,HttpStatus.UNAUTHORIZED,"만료된 토큰입니다. 토큰 재발급을 진행해주세요."),
    RE_LOGIN(445,HttpStatus.UNAUTHORIZED,"모든 토큰이 만료되었습니다. 재로그인해주세요."),
    INVALID_TOKEN(498,HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다.")
    ;

    private int code;
    private HttpStatus status;
    private String message;
}