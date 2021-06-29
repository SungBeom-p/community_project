package com.okay.Adapt;

import com.okay.domain.entity.User;
import com.okay.dto.UserDto;

public interface UserAdapt {
    User changeUser(UserDto userDto);

    UserDto changeUserDto(User user);

    UserDto nonMember(Long no,String id, String pw);
}
