package com.manjappa.store.dtos;

import com.manjappa.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message="Name must be less thann 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "email must be lowercase")  //custom validations annotaion
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 35, message = "Password must be between 6 to 24 characters")
    private String password;

}

