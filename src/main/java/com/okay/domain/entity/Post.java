package com.okay.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "post")
@Entity
public class Post {
    @Column(name = "post_no")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @Column(nullable = false)
    private Long views; // 조회 수

    @JoinColumn(name = "user_no")
    @ManyToOne(cascade = CascadeType.DETACH)
    private User userNo; // 사용자 고유 번호

    @Column(nullable = false, length = 20)
    private String name; // subName

    @Column(nullable = false, length = 20)
    private String pw;

    @Column(name="fileName")
    private String fileName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(name = "reg_date")
    private String regDate;

    @Column(name = "mod_date")
    private String modDate;

}
