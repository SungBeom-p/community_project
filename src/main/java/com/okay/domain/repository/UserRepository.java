package com.okay.domain.repository;

import com.okay.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUserIdAndUserPw(String userId, String userPw);
    List<User> findAll();

    
    //인환씨
    User findByUserNo (Long userNo);
}
