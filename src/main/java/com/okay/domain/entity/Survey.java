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
@Table(name="survey")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Column(name = "survey_no")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyNo;

    @Column(nullable = false)
    private Long views;

    @Column(nullable = false)
    private Long hit;

    @JoinColumn(name = "user_no")
    @ManyToOne(cascade = CascadeType.DETACH)
    private User userNo; // 사용자 고유 번호

    @Column(name="file_name1")
    private String fileName1;   //이미지이름

    @Column(name="file_name2")
    private String fileName2;   //이미지이름

    @Column
    private String option1; // 의견 1

    @Column
    private String option2; // 의견 2

    @Column(name="result1", nullable = false)
    private Long result1;

    @Column(name="result2", nullable = false)
    private Long result2;

    @Column(nullable = false, length = 20)
    private String name; // subName

    @Column(nullable = false, length = 20)
    private String pw;

    private String path;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "start_time")

    private String startTime;

    @Column(name = "end_time")
    private String endTime;
}



