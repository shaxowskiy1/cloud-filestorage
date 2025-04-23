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

    @Autowired
    private UserService userService;

	@BeforeEach
	void setUp(){
		userService.addUser(getVtbvtbvtb());
	}
	@Test
	void contextLoads() {
		User byUsername = userService.findByUsername("vtbvtbvtb");

		System.out.println(byUsername.toString());
	}

	private static SignUpRequestDTO getVtbvtbvtb() {
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
		signUpRequestDTO.setUsername("vtbvtbvtb");
		signUpRequestDTO.setPassword("vtbvtbvtb");
		signUpRequestDTO.setConfirmPassword("vtbvtbvtb");
		signUpRequestDTO.setFirstname("vtbvtbvtb");
		signUpRequestDTO.setLastname("vtbvtbvtb");
		return signUpRequestDTO;
	}

}
