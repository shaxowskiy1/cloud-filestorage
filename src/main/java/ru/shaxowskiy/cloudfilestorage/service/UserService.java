package ru.shaxowskiy.cloudfilestorage.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.exceptions.UserAlreadyExistInDatabase;
import ru.shaxowskiy.cloudfilestorage.models.Role;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.repositories.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(SignUpRequestDTO signUpRequestDTO){
        log.debug("Adding user from UserService: {}", signUpRequestDTO.getUsername());
        Optional<User> foundUser = userRepository.findByUsername(signUpRequestDTO.getUsername());
        if(foundUser.isPresent()){
            throw new UserAlreadyExistInDatabase("User with this username already exist");
        }
        User user = setFieldsToUser(signUpRequestDTO);
        userRepository.save(user);
    }

    private @NotNull User setFieldsToUser(SignUpRequestDTO signUpRequestDTO) {
        User user = new User();
        user.setUsername(signUpRequestDTO.getUsername());
        user.setFirstname(signUpRequestDTO.getFirstname());
        user.setLastname(signUpRequestDTO.getLastname());
       user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole(Collections.singleton(Role.USER));
        user.setActive(true);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        mapRole(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    private Collection<? extends GrantedAuthority> mapRole(Set<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
