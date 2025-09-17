package com.manjappa.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordDto {
     private String oldPassword;
     private String newPassword;
}
