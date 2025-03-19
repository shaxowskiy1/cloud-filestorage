package ru.shaxowskiy.cloudfilestorage.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class UserErrorResponse {
    private String message;
}
