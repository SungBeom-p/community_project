package com.okay.dto;
import com.okay.Adapt.UserAdapt;
import com.okay.domain.entity.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto implements UserAdapt {
    private Long userNo;
    private String userId;
    private String userPw;
    private String name;
    private String email;
    private Boolean gender;
    private String regDate;

    public UserDto(Long userNo){
        this.userNo = userNo;
    }
    @Override
    public User changeUser(UserDto userDto) {
        User user = User.builder()
                .userNo(userDto.getUserNo())
                .userId(userDto.getUserId())
                .userPw(userDto.getUserPw())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .regDate(userDto.getRegDate())
                .build();
        return user;
    }

    @Override
    public UserDto changeUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userPw(user.getUserPw())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .regDate(user.getRegDate())
                .build();
        return userDto;
    }

    public UserDto(Long userNo, String userId, String userPw){
        this.userNo = userNo;
        this.userId = userId;
        this.userPw = userPw;
    }

    @Override
    public UserDto nonMember(Long no, String id, String pw) { // 비회원 전용 change
        UserDto userDto = new UserDto(no,id,pw);
        return userDto;
    }
}
