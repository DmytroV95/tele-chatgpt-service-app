package com.varukha.telechatgptserviceapp.service;

import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.model.UserRegisterCode;

public interface UserRegisterCodeService {
    UserRegisterCode checkRegisterCodeIsValid(UserRegisterRequestDto request);
}
