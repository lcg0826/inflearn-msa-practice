package com.example.userservice.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUser {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not be less than two characters")
    private String email;

    @NotNull(message = "name cannot be null")
    @Size(min = 2, message = "name not be less than two characters")
    private String pwd;

    @NotNull(message = "pwd cannot be null")
    @Size(min = 8, message = "password must be equal or grater than 8 characters")

    private String name;
}
