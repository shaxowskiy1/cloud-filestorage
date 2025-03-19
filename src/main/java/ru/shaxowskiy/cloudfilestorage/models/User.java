package ru.shaxowskiy.cloudfilestorage.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 100, message = "Password should be between 8 and 30 characters")
    private String password;

    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 3, max = 30, message = "Firstname should be between 3 and 30 characters")
    @Column(name = "first_name")
    private String firstname;

    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 3, max = 30, message = "Lastname should be between 3 and 30 characters")
    @Column(name = "last_name")
    private String lastname;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "roles", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

}
