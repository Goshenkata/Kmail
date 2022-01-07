package com.app.Kmail.init;

import com.app.Kmail.service.EmailService;
import com.app.Kmail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBinit implements CommandLineRunner {

    private final UserService userService;
    private final EmailService emailService;
    private Logger LOGGER = LoggerFactory.getLogger(DBinit.class);

    public DBinit(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initUsers();
        emailService.initUsers();
        LOGGER.info("Database initialized");
    }
}
