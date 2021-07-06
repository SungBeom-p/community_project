package com.okay.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Table(name="user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column(name = "user_no")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "user_pw", nullable = false, length = 20)
    private String userPw;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private Boolean gender;

    @Column(name = "reg_date")
    private String regDate;

}
