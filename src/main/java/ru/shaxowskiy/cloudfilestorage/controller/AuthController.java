package ru.shaxowskiy.cloudfilestorage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.cloudfilestorage.dto.SignInRequestDTO;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.dto.AuthResponseDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserErrorResponse;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserNotCreatedException;
import ru.shaxowskiy.cloudfilestorage.repositories.UserRepository;
import ru.shaxowskiy.cloudfilestorage.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncode;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncode, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncode = passwordEncode;
        this.userRepository = userRepository;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody SignUpRequestDTO user,
                                                        BindingResult bindingResult) {
        log.debug("Received registration request for user: {}", user.getUsername());

        if (bindingResult.hasErrors()) {
            log.debug("Errors of validation in process registration user");
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getField() + " - " + fieldError.getDefaultMessage() + ".");
            }
            throw new UserNotCreatedException(sb.toString());
        }

        userService.addUser(user);
        return new ResponseEntity<>(new AuthResponseDTO(user.getUsername()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotCreatedException(UserNotCreatedException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@Valid @RequestBody SignInRequestDTO requestUser, HttpServletRequest request) {
        log.info("Received login request for user: {}", requestUser.getUsername());
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUser.getUsername(),
                            requestUser.getPassword()));
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticate);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            return ResponseEntity.ok(new AuthResponseDTO(requestUser.getUsername()));
        } catch(AuthenticationException e){
            log.info("Failed sign in {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    //TODO использовать маппер
    @GetMapping("/user/me")
    public ResponseEntity<?> profileUser(){
        log.info("Received profile request for user");

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(userService.findByUsername(name), HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser(){
        log.info("Received logout user");
        return ResponseEntity.noContent().build();
    }
}
