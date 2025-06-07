package ru.shaxowskiy.cloudfilestorage.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserAlreadyExistInDatabase;
import ru.shaxowskiy.cloudfilestorage.models.Role;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.repositories.UserRepository;
import ru.shaxowskiy.cloudfilestorage.service.impl.UserService;

import java.util.Collections;
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
    void testSaveUser_SavedUser_UserShouldBeSaving(){
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
    void testSaveUser_UserAlreadyExist_ThrowsException(){
        String username = "existingUser";
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUsername(username);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAlreadyExistInDatabase.class, () -> userService.addUser(signUpRequestDTO));
    }
    @Test
    void testLoadUser_User_ShouldReturnUserWithUsername(){
        String username = "Ivan";
        Role role = Role.ADMIN;
        String password = "password";
        User user = User
                .builder()
                .username(username)
                .password(password)
                .role(Collections.singleton(role))
                .build();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(username);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role)));
    }
    @Test
    void testLoadUser_UsernameNotFoundException_ThrowsException(){
        String username = "Ivan";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testFindUser_User_ShouldReturnUserWithUsername(){
        String username = "Ivan";
        User user = User
                .builder()
                .username(username)
                .build();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User foundUser = userService.findByUsername(username);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(username, foundUser.getUsername());
        Assertions.assertEquals(user, foundUser);
    }
    @Test
    void testFindUser_UsernameNotFoundException_ThrowsException(){
        String username = "Ivan";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }
}
