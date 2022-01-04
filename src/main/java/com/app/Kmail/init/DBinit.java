package com.app.Kmail.init;

import com.app.Kmail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBinit implements CommandLineRunner {

    private final UserService userService;
    private Logger LOGGER = LoggerFactory.getLogger(DBinit.class);

    public DBinit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initUsers();
        LOGGER.info("Database initialized");
    }
}
