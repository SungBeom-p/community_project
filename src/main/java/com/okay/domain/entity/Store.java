package com.okay.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name="store")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Column(name = "store_no")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeNo;

    @Column(nullable = false)
    private Long views;


    @JoinColumn(name = "user_no")
    @ManyToOne(cascade = CascadeType.ALL)
    private User userNo; // 사용자 고유 번호

    @Column(name="file_name")
    private String fileName;   //이미지이름

    @Column(name="map_id")
    private String mapId; //api

    @Column(name="size")
    private Long size; // 이미지크기

    @Column(nullable = false, length = 20)
    private String name; // subName


    @Column(nullable = false, length = 20)
    private String pw;


    @Column(nullable = false, length = 1000)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;
}
