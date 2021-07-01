package com.okay.service;

import com.okay.domain.entity.User;
import com.okay.domain.repository.UserRepository;
import com.okay.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
public class UserService extends Service {

    @Autowired
    UserRepository userRepository;


    public void create(UserDto userDto){ // DB CREATE
        User user = userDto.changeUser(userDto);
        userRepository.save(user);
    }

    public void remove(UserDto userDto){ // DB DELETE
        userRepository.deleteById(userDto.getUserNo());
    }

    public void update(UserDto userDto){ // DB UPDATE
        User user = userDto.changeUser(userDto);
        userRepository.save(user);
    }

    public void update(User user){ // DB UPDATE
        userRepository.save(user);
    }
    public UserDto selectOne(UserDto userDto){ // DB SELECT ONE <-DTO
        User user = userDto.changeUser(userDto);
        Optional<User> temp = userRepository.findById(user.getUserNo());
        UserDto result = userDto.changeUserDto(temp.get());
        return result;
    }

    public User selectOne(Long id){ // DB SELECT ONE <- ID
        Optional<User> result = userRepository.findById(id);
        return result.get();
    }

    public List<UserDto> selectAll(UserDto userDto){ // DB SELECT ALL <- DTO
        List<User> temp =userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        temp.forEach(i->{
            result.add(userDto.changeUserDto(i));
        });
        return result;
    }
    public List<User> selectAll(Long id){ // DB SEELCT ALL <- X
        return userRepository.findAll();
    }

    public UserDto login(String id, String pw){ // 로그인 체크
        User user = userRepository.findAllByUserIdAndUserPw(id, pw);
        UserDto userDto = new UserDto();

        if(user == null){
            userDto = userDto.nonMember(2L, id, pw);
        }else{
            userDto = userDto.changeUserDto(user);
        }

        if(userDto.getUserNo()>0){
            return userDto;
        }else{
            return new UserDto(2L);
        }
    }

    public HttpSession sessionAutowired(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("userId")==null){
            System.out.println("Null 일 경우 세션 값 : "+session.getAttribute("userId"));
            session.setAttribute("userId",2);
            return session;
        }else {
            System.out.println("Null이 아닐 경우 세션 값 : "+session.getAttribute("userId"));
            return session;
        }
    }
    public List<User> selectAllUser(UserDto userDto){
        return userRepository.findAll();
    }

    public List<User> allUser(){
        List<User> userlist = userRepository.findAll();
        return userlist;
    }
    public void remove(Long userNo){
        userRepository.deleteById(userNo);
    }
}
