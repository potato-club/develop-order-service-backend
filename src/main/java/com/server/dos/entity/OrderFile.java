package com.server.dos.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders_files")
public class OrderFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Orders orders;

    @Builder
    public OrderFile(String fileName, String s3Url, Orders orders) {
        this.fileName = fileName;
        this.s3Url = s3Url;
        this.orders = orders;
    }
}
