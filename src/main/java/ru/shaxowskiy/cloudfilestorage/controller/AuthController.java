package ru.shaxowskiy.cloudfilestorage.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.cloudfilestorage.dto.SignInRequestDTO;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpResponseDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserErrorResponse;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserNotCreatedException;
import ru.shaxowskiy.cloudfilestorage.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> registerUser(@Valid @RequestBody SignUpRequestDTO user,
                                                   BindingResult bindingResult){
        log.debug("Received registration request for user: {}", user.getUsername());

        if(bindingResult.hasErrors()){
            log.debug("Errors of validation in process registration user");
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getField() + " - " + fieldError.getDefaultMessage() + ".");
            }
            throw new UserNotCreatedException(sb.toString());
        }

        userService.addUser(user);

        return new ResponseEntity<>(new SignUpResponseDTO(user.getUsername()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotCreatedException(UserNotCreatedException e){
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    //Приходит запрос с JSON
    @PostMapping("/sign-in")
    public ResponseEntity<HttpStatus> loginUser(@Valid @RequestBody SignInRequestDTO user,
                                                   BindingResult bindingResult){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Received login request for user: {}", user.getUsername());
        //log.debug("Principal login request for user: {}", principal);
        //userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
