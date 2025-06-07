package ru.shaxowskiy.cloudfilestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.service.impl.UserService;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CloudFilestorageApplicationTests {

}
