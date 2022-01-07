package com.app.Kmail.controllers;

import com.app.Kmail.model.binding.EmailSendBindingModel;
import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.repository.EmailRepository;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user")
class EmailControllerTest {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static final MockMultipartFile MULTIPARTFILE = new MockMultipartFile("test.txt", "Hello world!".getBytes());
    private static final String FROM = "user@kmail.com";
    private static final String TO = "debian@kmail.com";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    @Test
    void getSendEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/send"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("send-email"));
    }


    @Test
    void sendValidEmail() throws Exception {
        EmailSendBindingModel emailSendBindingModel = new EmailSendBindingModel();
        emailSendBindingModel.setFrom(FROM)
                .setTo(TO)
                .setTitle(TITLE)
                .setContent(CONTENT)
                .setAttachment(MULTIPARTFILE);

        mockMvc.perform(MockMvcRequestBuilders.post("/emails/send")
                        .flashAttr("emailSendBindingModel", emailSendBindingModel)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        System.out.println("------------");
        for (UserEntity user : userRepository.findAll()) {
            System.out.println(user.getUsername());
        }
        System.out.println("------------");

        UserEntity userFrom = userService.findByUsernameWithAddress(FROM)
                        .orElseThrow(() -> new UsernameNotFoundException("username " + FROM  + " not found"));
        UserEntity userTo = userService.findByUsernameWithAddress(TO)
                        .orElseThrow(() -> new UsernameNotFoundException("username " + TO + "not found"));

        Assertions.assertEquals(1, emailRepository.count());
        EmailEntity emailEntity = emailRepository.findAll().get(0);
        Assertions.assertEquals(userFrom, emailEntity.getFrom());
        Assertions.assertEquals(userTo, emailEntity.getTo());
        Assertions.assertEquals(TITLE, emailEntity.getTitle());
        Assertions.assertEquals(CONTENT, emailEntity.getContent());
        Assertions.assertArrayEquals(MULTIPARTFILE.getBytes(), emailEntity.getAttachment());
    }

    @BeforeEach
    void setUp() {
        userService.initUsers();
    }

    @AfterEach
    void tearDown() {
        emailRepository.deleteAll();
        userService.deleteAll();}
}