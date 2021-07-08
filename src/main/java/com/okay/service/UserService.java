package com.okay.service;

import com.okay.domain.entity.User;
import com.okay.domain.repository.UserRepository;
import com.okay.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
public class UserService extends Service {

    @Autowired
    UserRepository userRepository;


    public void create(UserDto userDto) {// DB CREATE
        User user = userDto.changeUser(userDto);
        userRepository.save(user);
    }

    public void update(UserDto userDto) { // DB UPDATE
        User user = userDto.changeUser(userDto);
        userRepository.save(user);
    }

    public User selectOne(Long id) { // DB SELECT ONE <- ID
        Optional<User> result = userRepository.findById(id);
        return result.get();
    }

    public UserDto login(String id, String pw) { // 로그인 체크
        User user = userRepository.findAllByUserIdAndUserPw(id, pw);
        UserDto userDto = new UserDto();
        if (user == null) {
            userDto = userDto.nonMember(2L, id, pw);
        } else {
            userDto = userDto.changeUserDto(user);
        }
        if (userDto.getUserNo() > 0) {
            return userDto;
        } else {
            return new UserDto(2L);
        }
    }

    public HttpSession sessionAutowired(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            session.setAttribute("userId", 2);
            return session;
        } else {
            return session;
        }
    }

    public List<User> allUser() {
        List<User> userlist = userRepository.findAll();
        return userlist;
    }

    public void remove(Long userNo) {
        userRepository.deleteById(userNo);
    }

    //인환씨
    public User getUser(Long userNo) {
        User user = userRepository.findById(userNo).get();
        return user;
    }


    public Long max() {

        BigDecimal max = userRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }

    public List<User> idCheck(String userId) {
         List<User> user = userRepository.findAllByUserId(userId);
        return user;
    }
}
