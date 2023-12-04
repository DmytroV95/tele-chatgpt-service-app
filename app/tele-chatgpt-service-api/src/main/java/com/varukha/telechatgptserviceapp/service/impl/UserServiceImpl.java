package com.varukha.telechatgptserviceapp.service.impl;

import static com.varukha.telechatgptserviceapp.model.Role.RoleName.ROLE_ADMIN;

import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterResponseDto;
import com.varukha.telechatgptserviceapp.exception.EntityNotFoundException;
import com.varukha.telechatgptserviceapp.exception.RegistrationException;
import com.varukha.telechatgptserviceapp.mapper.UserMapper;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.repository.RoleRepository;
import com.varukha.telechatgptserviceapp.repository.UserRepository;
import com.varukha.telechatgptserviceapp.service.UserRegisterCodeService;
import com.varukha.telechatgptserviceapp.service.UserService;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRegisterCodeService userRegisterCodeService;

    @Override
    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto request)
            throws RegistrationException {
        if (isUserExist(request.getEmail())) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = createUser(request);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public User getActiveAdmin() {
        return userRepository.findAll()
                .stream()
                .filter(User::getIsActive)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with active status"));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("Can't find a user by provided email "
                        + authentication.getName())
        );
    }

    @Override
    public void updateIsActiveStatus(boolean isActive) {
        User authenticatedUser = getAuthenticatedUser();
        authenticatedUser.setIsActive(isActive);
        userRepository.save(authenticatedUser);
    }

    private User createUser(UserRegisterRequestDto request) {
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(false);
        Role defaultRole = getDefaultRole();
        user.setRoles(Set.of(defaultRole));
        user.setRegisterCode(userRegisterCodeService
                .checkRegisterCodeIsValid(request));
        return user;
    }

    private boolean isUserExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private Role getDefaultRole() {
        return roleRepository.findByRoleName(ROLE_ADMIN)
                .orElseThrow(() -> new NoSuchElementException("The role "
                        + ROLE_ADMIN + " is not found in the database"));
    }
}
