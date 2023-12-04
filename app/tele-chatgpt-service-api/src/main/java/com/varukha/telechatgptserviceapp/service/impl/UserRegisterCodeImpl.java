package com.varukha.telechatgptserviceapp.service.impl;

import com.varukha.telechatgptserviceapp.dto.user.UserRegisterRequestDto;
import com.varukha.telechatgptserviceapp.model.UserRegisterCode;
import com.varukha.telechatgptserviceapp.repository.UserRegisterCodeRepository;
import com.varukha.telechatgptserviceapp.service.UserRegisterCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterCodeImpl implements UserRegisterCodeService {
    private final UserRegisterCodeRepository codeRepository;

    @Override
    public UserRegisterCode checkRegisterCodeIsValid(UserRegisterRequestDto request) {
        String providedCode = request.getRegisterCode();
        UserRegisterCode userRegisterCode = codeRepository
                .findByIsUsedAndRegisterCode(false, providedCode)
                .filter(code -> providedCode.equals(code.getRegisterCode()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid register code"));
        userRegisterCode.setUsed(true);
        codeRepository.save(userRegisterCode);
        return userRegisterCode;
    }
}
