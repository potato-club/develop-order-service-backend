package com.server.dos.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String clientEmail;
    @Column(nullable = false)
    private String hotLine;

    private String subLine;
}
