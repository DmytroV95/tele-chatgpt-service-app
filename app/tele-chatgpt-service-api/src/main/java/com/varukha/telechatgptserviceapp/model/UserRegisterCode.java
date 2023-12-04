package com.varukha.telechatgptserviceapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_register_code")
public class UserRegisterCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register_code",
            nullable = false,
            unique = true)
    private String registerCode;

    @Column(name = "is_used")
    private boolean isUsed = false;
}
