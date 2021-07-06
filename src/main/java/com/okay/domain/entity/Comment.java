package com.okay.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Comment {

    @Column(name = "comment_no")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @JoinColumn(name = "post_no")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Post postNo;

    @JoinColumn(name = "user_no")
    @ManyToOne(cascade = CascadeType.DETACH)
    private User userNo;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length =20)
    private String pw;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "reg_date")
    private String regDate;

}
