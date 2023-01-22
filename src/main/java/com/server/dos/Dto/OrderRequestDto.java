package com.server.dos.Dto;

import com.server.dos.Entity.Order;
import com.server.dos.Enum.OrderState;
import com.server.dos.Enum.SiteOwner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Data
public class OrderRequestDto {
    @ApiModelProperty(value = "사이트 이름", example = "DOS", required = true)
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "나만의 웹사이트 만들고 싶어서", required = true)
    private String purpose;
    @ApiModelProperty(value = "운영자", example = "PERSONAL", required = true)
    private SiteOwner owner;
    @ApiModelProperty(value = "브랜드 컬러", example = "['#FFFFFF', '#000000']", required = true)
    private List<String> brandColor;
    @ApiModelProperty(value = "페이지 수", example = "1", required = true)
    private int page;
    @ApiModelProperty(value = "로그인 여부", example = "false", required = true)
    private Boolean login;
    @ApiModelProperty(value = "데이터베이스 여부", example = "false", required = true)
    private Boolean database;
    @ApiModelProperty(value = "완성 희망 기간", example = "3")
    private int duration;
    @ApiModelProperty(value = "희망 미팅 날짜", example = "2023-03-03 10:30", required = true)
    private String meeting;

    public Order toEntity() {
        return Order.builder()
                .siteName(siteName)
                .purpose(purpose)
                .owner(owner)
                .state(OrderState.WORKING)
                .brandColor(brandColor)
                .database(database)
                .duration(duration)
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
