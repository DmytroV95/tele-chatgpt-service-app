package com.varukha.telechatgptserviceapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.dto.user.UserRegisterResponseDto;
import com.varukha.telechatgptserviceapp.exception.EntityNotFoundException;
import com.varukha.telechatgptserviceapp.exception.RegistrationException;
import com.varukha.telechatgptserviceapp.mapper.UserMapper;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.model.UserRegisterCode;
import com.varukha.telechatgptserviceapp.repository.RoleRepository;
import com.varukha.telechatgptserviceapp.repository.UserRepository;
import com.varukha.telechatgptserviceapp.service.impl.UserRegisterCodeImpl;
import com.varukha.telechatgptserviceapp.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final UserRegisterRequestDto USER_REGISTER_REQUEST_DTO =
            new UserRegisterRequestDto();
    private static final UserRegisterResponseDto USER_REGISTER_RESPONSE_DTO =
            new UserRegisterResponseDto();
    private static final UserRegisterCode USER_REGISTER_CODE_1 =
            new UserRegisterCode();

    private static final UserRegisterCode USER_REGISTER_CODE_2 =
            new UserRegisterCode();
    private static final Role ROLE_ADMIN = new Role();
    private static final User TEST_USER_1 = new User();
    private static final User TEST_USER_2 = new User();

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRegisterCodeImpl userRegisterCodeService;

    @BeforeAll
    public static void setUp() {
        USER_REGISTER_CODE_1.setId(1L);
        USER_REGISTER_CODE_1.setRegisterCode("1");
        USER_REGISTER_CODE_1.setUsed(false);

        USER_REGISTER_CODE_2.setId(2L);
        USER_REGISTER_CODE_2.setRegisterCode("2");
        USER_REGISTER_CODE_2.setUsed(false);

        ROLE_ADMIN.setId(1L);
        ROLE_ADMIN.setRoleName(Role.RoleName.ROLE_ADMIN);

        TEST_USER_1.setId(1L);
        TEST_USER_1.setEmail("testUser1@email.com");
        TEST_USER_1.setPassword("testPassword1");
        TEST_USER_1.setFirstName("testFirstName1");
        TEST_USER_1.setLastName("testLastName1");
        TEST_USER_1.setIsActive(true);
        TEST_USER_1.setRoles(Set.of(ROLE_ADMIN));
        TEST_USER_1.setRegisterCode(USER_REGISTER_CODE_1);

        TEST_USER_2.setId(2L);
        TEST_USER_2.setEmail("testUser2@email.com");
        TEST_USER_2.setPassword("testPassword2");
        TEST_USER_2.setFirstName("testFirstName2");
        TEST_USER_2.setLastName("testLastName2");
        TEST_USER_2.setIsActive(false);
        TEST_USER_2.setRoles(Set.of(ROLE_ADMIN));
        TEST_USER_2.setRegisterCode(USER_REGISTER_CODE_2);

        USER_REGISTER_REQUEST_DTO.setEmail(TEST_USER_1.getEmail());
        USER_REGISTER_REQUEST_DTO.setPassword(TEST_USER_1.getPassword());
        USER_REGISTER_REQUEST_DTO.setRepeatPassword(TEST_USER_1.getPassword());
        USER_REGISTER_REQUEST_DTO.setFirstName(TEST_USER_1.getFirstName());
        USER_REGISTER_REQUEST_DTO.setLastName(TEST_USER_1.getLastName());
        USER_REGISTER_REQUEST_DTO.setRegisterCode(
                String.valueOf(TEST_USER_1.getRegisterCode()));

        USER_REGISTER_RESPONSE_DTO.setId(TEST_USER_1.getId());
        USER_REGISTER_RESPONSE_DTO.setEmail(TEST_USER_1.getEmail());
        USER_REGISTER_RESPONSE_DTO.setFirstName(TEST_USER_1.getFirstName());
        USER_REGISTER_RESPONSE_DTO.setLastName(TEST_USER_1.getLastName());
    }

    @Test
    @DisplayName("""
            Test the 'register' method with valid request parameters
            to add new user with admin role to the system
            """)
    void register_ValidUserData_ReturnUserRegisterResponseDto() {
        when(roleRepository.findByRoleName(ROLE_ADMIN.getRoleName()))
                .thenReturn(Optional.of(ROLE_ADMIN));
        when(userMapper.toModel(USER_REGISTER_REQUEST_DTO)).thenReturn(TEST_USER_1);
        when(passwordEncoder.encode(USER_REGISTER_REQUEST_DTO.getPassword()))
                .thenReturn("encodedPassword");
        when(userRegisterCodeService.checkRegisterCodeIsValid(USER_REGISTER_REQUEST_DTO))
                .thenReturn(USER_REGISTER_CODE_1);
        when(userRepository.save(TEST_USER_1)).thenReturn(TEST_USER_1);
        when(userMapper.toUserResponseDto(TEST_USER_1))
                .thenReturn(USER_REGISTER_RESPONSE_DTO);

        UserRegisterResponseDto actualResult = userService.register(USER_REGISTER_REQUEST_DTO);

        assertNotNull(actualResult);
        assertEquals(USER_REGISTER_RESPONSE_DTO, actualResult);
    }

    @Test
    @DisplayName("""
            Test the 'register' method with not existed user email
            """)
    void register_UserEmailNotExists_ReturnRegistrationException() {
        when(userRepository.findUserByEmail(anyString()))
                .thenReturn((Optional.of(mock(User.class))));
        assertThrows(RegistrationException.class,
                () -> userService.register(USER_REGISTER_REQUEST_DTO));
    }

    @Test
    @DisplayName("""
            Test the 'getActiveAdmin' method with user active status false
            """)
    void getActiveAdmin_AdminIsActiveFalse_ReturnEntityNotFoundException() {
        when(userRepository.findAll())
                .thenReturn(List.of(TEST_USER_2));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getActiveAdmin());

        assertThrows(EntityNotFoundException.class,
                () -> userService.getActiveAdmin());
        assertEquals("Can't find user with active status",
                exception.getMessage());
    }
}
