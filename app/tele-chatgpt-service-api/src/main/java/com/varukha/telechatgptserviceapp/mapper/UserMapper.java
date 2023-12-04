package com.varukha.telechatgptserviceapp.mapper;

import com.varukha.telechatgptserviceapp.config.MapperConfig;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterResponseDto;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.model.UserRegisterCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegisterResponseDto toUserResponseDto(User user);

    @Mapping(target = "registerCode",
            source = "requestDto.registerCode")
    User toModel(UserRegisterRequestDto requestDto);

    default UserRegisterCode map(String value) {
        UserRegisterCode userRegisterCode = new UserRegisterCode();
        userRegisterCode.setRegisterCode(value);
        return userRegisterCode;
    }
}
