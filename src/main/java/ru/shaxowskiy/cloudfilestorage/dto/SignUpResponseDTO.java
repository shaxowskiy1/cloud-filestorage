package ru.shaxowskiy.cloudfilestorage.dto;

import lombok.Data;

@Data
public class SignUpResponseDTO {
    private String username;

    public SignUpResponseDTO(String username) {
        this.username = username;
    }
}
