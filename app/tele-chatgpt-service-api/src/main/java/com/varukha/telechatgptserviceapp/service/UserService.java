package com.varukha.telechatgptserviceapp.service;

import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterResponseDto;
import com.varukha.telechatgptserviceapp.exception.RegistrationException;
import com.varukha.telechatgptserviceapp.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserRegisterResponseDto register(UserRegisterRequestDto registrationRequestDto)
            throws RegistrationException;

    User getActiveAdmin();

    User getAuthenticatedUser();

    void updateIsActiveStatus(boolean isActive);
}
