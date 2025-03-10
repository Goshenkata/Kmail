package com.app.Kmail.init;

import com.app.Kmail.exceptions.DbSchemaVersionMismatchException;
import com.app.Kmail.repository.SchemaVersionRepository;
import com.app.Kmail.service.EmailService;
import com.app.Kmail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBinit implements CommandLineRunner {

    private final UserService userService;
    private final EmailService emailService;
    private final SchemaVersionRepository dbVersionRepository;

    @Value("${schema.version}")
    private int currentSchemaVersion;
    private Logger LOGGER = LoggerFactory.getLogger(DBinit.class);

    public DBinit(UserService userService, EmailService emailService, SchemaVersionRepository dbVersionRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.dbVersionRepository = dbVersionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Integer latestSchemaVersion = dbVersionRepository.findLatestVersion();

        if (latestSchemaVersion == null || latestSchemaVersion != currentSchemaVersion) {
            String message = String.format("Shema version mismatch, latest version is %d but the version for this app is %d" , latestSchemaVersion, currentSchemaVersion);
            throw new DbSchemaVersionMismatchException(message);
        }
        userService.initUsers();
        emailService.initEmail();
        LOGGER.info("Database initialized");
    }
}
