package ru.shaxowskiy.cloudfilestorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserNotCreatedException;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registration(){
        return null;
    }

    //TODO сервис не входит в if при нулевых полях - ru.shaxowskiy.cloudfilestorage.exceptions.UserAlreadyExistInDatabase: User with this username already exist null
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody User user,
                                                   BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.debug("Errors of validation in process registration user");
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getField() + " - " + fieldError.getDefaultMessage() + "\n");
            }
            throw new UserNotCreatedException(sb.toString());
        }
        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
