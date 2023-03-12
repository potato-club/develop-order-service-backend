package com.server.dos.dto;

import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class MeetingDateDto {
    private String name;
    private String date;
    private String time;

    public MeetingDateDto(OrderMeetingDto meetingDto) {
        this.name = meetingDto.getName();
        this.date = meetingDto.getMeeting().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = meetingDto.getMeeting().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
