package ru.shaxowskiy.cloudfilestorage.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String username;

    public AuthResponseDTO(String username) {
        this.username = username;
    }
}
