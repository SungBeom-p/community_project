package com.okay.domain.repository;

import com.okay.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUserIdAndUserPw(String userId, String userPw);
    List<User> findAll();


    //dto 넘버 증감
    @Query(value = "select max(userNo) from User")
    BigDecimal max();


    //인환씨
    User findByUserNo (Long userNo);
}
