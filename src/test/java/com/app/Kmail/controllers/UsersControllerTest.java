package com.app.Kmail.controllers;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String USERNAME = "UserUserov";
    private static final String PASSWORD = "Hunter222";
    private static final String FIRST_NAME = "User";
    private static final String LAST_NAME = "Userov";

    @Test
    void testRegister() throws Exception {
        System.out.println(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    void testRegisterPost() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .param("confirmPassword", PASSWORD)
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        Assertions.assertEquals(1, userRepository.count(), "after creating a single user the count is one");
        Optional<UserEntity> userOpt = userRepository.findByUsername(USERNAME);
        Assertions.assertTrue(userOpt.isPresent(), "the new user is present in the database");
        UserEntity user = userOpt.get();
        Assertions.assertEquals(USERNAME, user.getUsername(), "the username matches");
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, user.getPassword()), "decoded password matches");
        Assertions.assertEquals(FIRST_NAME, user.getFirstName(), "the first name matches");
        Assertions.assertEquals(LAST_NAME, user.getLastName(), "the last name matches");
    }

    @Test
    void getLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    void postLogin() throws Exception {
        createUser();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                .param(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, USERNAME)
                .param(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, PASSWORD)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    private void createUser() {
        UserEntity user = new UserEntity();
        user.setUsername(USERNAME)
                .setPassword(passwordEncoder.encode(PASSWORD))
                .setLastName(LAST_NAME)
                .setFirstName(FIRST_NAME);
        userRepository.save(user);
    }
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
}