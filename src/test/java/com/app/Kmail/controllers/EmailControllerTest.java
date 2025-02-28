package com.app.Kmail.controllers;

import com.app.Kmail.model.binding.EmailSendBindingModel;
import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.repository.EmailRepository;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.EmailService;
import com.app.Kmail.service.UserService;
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


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailControllerTest {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

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
                .setContent(CONTENT);

        mockMvc.perform(MockMvcRequestBuilders.post("/emails/send")
                        .flashAttr("emailSendBindingModel", emailSendBindingModel)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

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
    }


    @Test
    void sendEmailToNonExistentUser() throws Exception {
        EmailSendBindingModel emailSendBindingModel = new EmailSendBindingModel();
        emailSendBindingModel.setFrom(FROM)
                .setTo("Invalid343543@kmail.com")
                .setTitle(null)
                .setContent(CONTENT)
                .setAttachment(MULTIPARTFILE);

        mockMvc.perform(MockMvcRequestBuilders.post("/emails/send")
                        .flashAttr("emailSendBindingModel", emailSendBindingModel)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/emails/send"));
        Assertions.assertEquals(0, emailRepository.count());
    }

    @Test
    void sendNullEmail() throws Exception {
        EmailSendBindingModel emailSendBindingModel = new EmailSendBindingModel();
        emailSendBindingModel.setFrom(FROM)
                .setTo(TO)
                .setTitle(null)
                .setContent(CONTENT)
                .setAttachment(MULTIPARTFILE);

        mockMvc.perform(MockMvcRequestBuilders.post("/emails/send")
                        .flashAttr("emailSendBindingModel", emailSendBindingModel)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/emails/send"));
        Assertions.assertEquals(0, emailRepository.count());
    }


    @Test
    void sendInvalidUser() throws Exception {
        EmailSendBindingModel emailSendBindingModel = new EmailSendBindingModel();
        emailSendBindingModel.setFrom("manjaro")
                .setTo(TO)
                .setTitle(null)
                .setContent(CONTENT)
                .setAttachment(MULTIPARTFILE);

        mockMvc.perform(MockMvcRequestBuilders.post("/emails/send")
                        .flashAttr("emailSendBindingModel", emailSendBindingModel)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/emails/send"));
        Assertions.assertEquals(0, emailRepository.count());
    }

    @Test
    void inbox() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/inbox")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("inbox"));
    }


    @Test
    void sent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/sent")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("inbox"));
    }

    @Test
    void viewEmailValid() throws Exception {
        emailService.initEmail();
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/9")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("email"));
    }


    @Test
    void viewEmailInvalid() throws Exception {
        emailService.initEmail();
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/4")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void viewDownloadInvalid() throws Exception {
        emailService.initEmail();
        mockMvc.perform(MockMvcRequestBuilders.get("/emails/4/download")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @BeforeAll
    void initDelete() {
        emailRepository.deleteAll();
        userRepository.deleteAll();
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