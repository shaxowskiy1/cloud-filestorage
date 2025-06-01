package ru.shaxowskiy.cloudfilestorage.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserAlreadyExistInDatabase;
import ru.shaxowskiy.cloudfilestorage.models.Role;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.repositories.UserRepository;
import ru.shaxowskiy.cloudfilestorage.service.impl.UserService;

import java.util.Collections;

@SpringBootTest
@Testcontainers
public class UserServiceIT {
    @Autowired
    private UserService userService;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser_SavedUser_UserShouldBeSaving(){
        String username = "testuser";
        String password = "testpassword";
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUsername(username);
        signUpRequestDTO.setPassword(password);
        signUpRequestDTO.setConfirmPassword(password);
        signUpRequestDTO.setFirstname("test");
        signUpRequestDTO.setLastname("test");

        userService.addUser(signUpRequestDTO);

        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Process find is failed with username " + username));

        Assertions.assertFalse(userRepository.findByUsername(username).isEmpty());
        Assertions.assertEquals(username, foundUser.getUsername());
    }
    @Test
    void testSaveUser_UserAlreadyExist_ThrowsException(){
        String username = "testuser";
        String password = "testpassword";
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUsername(username);
        signUpRequestDTO.setPassword(password);
        signUpRequestDTO.setConfirmPassword(password);
        signUpRequestDTO.setFirstname("test");
        signUpRequestDTO.setLastname("test");
        userService.addUser(signUpRequestDTO);


        Assertions.assertThrows(UserAlreadyExistInDatabase.class, () -> userService.addUser(signUpRequestDTO));
    }

    @Test
    void testFindUser_User_ShouldReturnUserWithUsername(){
        String username = "Ivan";
        Role role = Role.ADMIN;
        String password = "password";
        User user = User
                .builder()
                .username(username)
                .password(password)
                .lastname("test")
                .firstname("test")
                .role(Collections.singleton(role))
                .build();
        userRepository.save(user);

        User foundUser = userService.findByUsername(username);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(username, foundUser.getUsername());
        Assertions.assertEquals(user, foundUser);
    }
    @Test
    void testFindUser_UsernameNotFoundException_ThrowsException(){
        String username = "Ivan";

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));

    }
}
