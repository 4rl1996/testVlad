package com.example.qaservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleDto {

    @NotBlank(message = "Укажите логин!")
    private String login;

    @NotBlank(message = "Укажите пароль!")
    private String password;
}
