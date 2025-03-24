package ru.shaxowskiy.cloudfilestorage.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 100, message = "Password should be between 8 and 30 characters")
    private String password;
}
