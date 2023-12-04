package com.varukha.telechatgptserviceapp.dto.user;

import com.varukha.telechatgptserviceapp.service.validate.FieldMatch;
import com.varukha.telechatgptserviceapp.service.validate.PasswordValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords don't match!"
)
@Getter
@Setter
public class UserRegisterRequestDto {
    @Email
    private String email;
    @NotEmpty
    @PasswordValidator
    private String password;
    private String repeatPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String registerCode;

}
