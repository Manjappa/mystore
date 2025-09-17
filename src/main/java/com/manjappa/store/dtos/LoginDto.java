package com.manjappa.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "Email is required!")
    @Email
    private final String email;

    @NotBlank(message = "Password is required.")
    private final String password;
}
