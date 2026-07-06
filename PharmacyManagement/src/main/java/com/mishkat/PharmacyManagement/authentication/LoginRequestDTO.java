package com.mishkat.PharmacyManagement.authentication;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String usernameOrEmail;
    private String password;
}
