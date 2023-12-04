package com.varukha.telechatgptserviceapp.repository;

import com.varukha.telechatgptserviceapp.model.UserRegisterCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRegisterCodeRepository extends JpaRepository<UserRegisterCode, Long> {
    @Query("FROM UserRegisterCode c"
            + " WHERE c.isUsed = :isUsed"
            + " AND c.registerCode = :providedCode")
    Optional<UserRegisterCode> findByIsUsedAndRegisterCode(boolean isUsed,
                                                           String providedCode);


}
