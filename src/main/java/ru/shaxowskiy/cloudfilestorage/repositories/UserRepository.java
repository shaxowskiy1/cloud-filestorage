package ru.shaxowskiy.cloudfilestorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shaxowskiy.cloudfilestorage.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<User> findAllUsers();
}
