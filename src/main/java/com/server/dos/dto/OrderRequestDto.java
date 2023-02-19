package com.server.dos.dto;

import com.server.dos.entity.Client;
import com.server.dos.entity.Order;
import com.server.dos.Enum.SiteOwner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class OrderRequestDto {
    @ApiModelProperty(value = "이름", example = "박청조", required = true)
    private String name;
    @ApiModelProperty(value = "이메일", example = "test@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "전화번호", example = "010-0000-0000", required = true)
    private String hotLine;
    @ApiModelProperty(value = "전화번호2", example = "010-1234-1234")
    private String subLine;
    @ApiModelProperty(value = "사이트 이름", example = "DOS", required = true)
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "나만의 웹사이트 만들고 싶어서", required = true)
    private String purpose;
    @ApiModelProperty(value = "운영자", example = "PERSONAL", required = true)
    private SiteOwner owner;
    @ApiModelProperty(value = "브랜드 컬러1", example = "['#FFFFFF', '#000000']", required = true)
    private List<String> mainColor;
    @ApiModelProperty(value = "브랜드 컬러2", example = "['#FFFFFF', '#000000']")
    private List<String> subColor;
    @ApiModelProperty(value = "페이지 수", example = "1", required = true)
    private Integer page;
    @ApiModelProperty(value = "로그인 여부", example = "false", required = true)
    private Boolean login;
    @ApiModelProperty(value = "데이터베이스 여부", example = "false", required = true)
    private Boolean database;
    @ApiModelProperty(value = "희망 미팅 날짜", example = "2023-03-03 10:30", required = true)
    private String meeting;

    public Order toEntity() {
        return Order.builder()
                .client(new Client(name, email, hotLine, subLine))
                .siteName(siteName)
                .purpose(purpose)
                .owner(owner)
                .mainColor(mainColor)
                .subColor(subColor)
                .database(database)
                .login(login)
                .meeting(parseDate(meeting))
                .page(page)
                .build();
    }

    public LocalDateTime parseDate(String meeting) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(meeting, formatter);
    }
}
