package com.app.Kmail.service;

import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.impl.KmailUserServiceImpl;
import com.app.Kmail.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private KmailUserServiceImpl mockKmailUserService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new Pbkdf2PasswordEncoder();
        userService = new UserServiceImpl(mockUserRepository, passwordEncoder, mockKmailUserService);
    }


    @Test()
    @DisplayName("check if the username does not contain special characters")
    void isUsernameValid() {
        Assertions.assertTrue(userService.isUsernameValid("Th1sName1sVeryVal1d"), "check a valid username");
        Assertions.assertFalse(userService.isUsernameValid("Th1s_N@me1s-Very]nVal1d"));
    }

    @Test
    void removeEmailAddress() {
        Assertions.assertEquals("username",userService.removeAddress("username@kmail.com"));
        Assertions.assertThrows(IllegalArgumentException.class,() -> userService.removeAddress("username"));
    }

}