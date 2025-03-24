package ru.shaxowskiy.cloudfilestorage.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 100, message = "Password should be between 8 and 30 characters")
    private String password;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 30, message = "Password should be between 8 and 30 characters")
    private String confirmPassword;

    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 3, max = 30, message = "Firstname should be between 3 and 30 characters")
    @Column(name = "first_name")
    private String firstname;

    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 3, max = 30, message = "Lastname should be between 3 and 30 characters")
    @Column(name = "last_name")
    private String lastname;
}
