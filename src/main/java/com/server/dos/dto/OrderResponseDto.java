package com.server.dos.dto;

import com.server.dos.Enum.SiteOwner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    @ApiModelProperty(value = "이름", example = "박청조")
    private String name;
    @ApiModelProperty(value = "이메일", example = "test@naver.com")
    private String email;
    @ApiModelProperty(value = "전화번호", example = "010-0000-0000")
    private String hotLine;
    @ApiModelProperty(value = "전화번호2", example = "010-1234-1234")
    private String subLine;
    @ApiModelProperty(value = "사이트 이름", example = "DOS")
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "나만의 웹사이트 만들고 싶어서")
    private String purpose;
    @ApiModelProperty(value = "운영자", example = "PERSONAL")
    private SiteOwner owner;
    @ApiModelProperty(value = "브랜드 컬러1", example = "['#FFFFFF', '#000000']")
    private List<String> mainColor;
    @ApiModelProperty(value = "브랜드 컬러2", example = "['#FFFFFF', '#000000']")
    private List<String> subColor;
    @ApiModelProperty(value = "페이지 수", example = "1")
    private Integer page;
    @ApiModelProperty(value = "로그인 여부", example = "false")
    private Boolean login;
    @ApiModelProperty(value = "데이터베이스 여부", example = "false")
    private Boolean database;
    @ApiModelProperty(value = "희망 미팅 날짜")
    private LocalDateTime meeting;
    @ApiModelProperty(value = "기타 추가 사항", example = "대충 만들면 돈 안줄겁니다 호호")
    private String etc;
    @ApiModelProperty(value = "참고 자료 리스트")
    private List<FileDto> files;
}
