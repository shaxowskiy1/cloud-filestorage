package ru.shaxowskiy.cloudfilestorage.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserAlreadyExistInDatabase;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.repositories.UserRepository;
import ru.shaxowskiy.cloudfilestorage.service.impl.UserService;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;


    @Test
    void checkSaveUser_SavedUser_UserShouldBeSaving(){
        PasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
        SignUpRequestDTO requestDTO = new SignUpRequestDTO();
        String username = "newUser";
        requestDTO.setUsername(username);
        String password = "password";
        requestDTO.setPassword(passwordEncoder1.encode(password));
        requestDTO.setConfirmPassword(passwordEncoder1.encode(password));
        String firstname = "Test";
        requestDTO.setFirstname(firstname);
        String lastname = "Testov";
        requestDTO.setLastname(lastname);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.addUser(requestDTO);

        Mockito.verify(userRepository).findByUsername("newUser");
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void checkSaveUser_UserAlreadyExist_ThrowsException(){
        String username = "existingUser";
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUsername(username);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAlreadyExistInDatabase.class, () -> userService.addUser(signUpRequestDTO));
    }
}
