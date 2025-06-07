package ru.shaxowskiy.cloudfilestorage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistInDatabase.class)
    public ResponseEntity<UserErrorResponse> handleUserAlreadyExist(UserAlreadyExistInDatabase e){
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.CONFLICT);
    }
}
