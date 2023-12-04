package com.varukha.telechatgptserviceapp.controller;

import com.varukha.telechatgptserviceapp.dto.user.UserLoginRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserLoginResponseDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterResponseDto;
import com.varukha.telechatgptserviceapp.security.AuthenticationService;
import com.varukha.telechatgptserviceapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller",
        description = "Endpoints for managing login and register user")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user to system")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    public UserRegisterResponseDto register(
            @RequestBody @Valid UserRegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }
}
