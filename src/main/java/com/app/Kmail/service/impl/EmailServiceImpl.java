package com.app.Kmail.service.impl;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.model.view.EmailViewModel;
import com.app.Kmail.repository.EmailRepository;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EmailEntity save(EmailServiceModel emailServiceModel) {
        EmailEntity email = new EmailEntity();
        email.setAttachment(emailServiceModel.getAttachment())
                .setContent(emailServiceModel.getContent())
                .setCreated(emailServiceModel.getCreated())
                .setRead(emailServiceModel.isRead())
                .setTitle(emailServiceModel.getTitle())
                .setFrom(userRepository.findByUsername(emailServiceModel
                                .getFrom())
                        .orElseThrow(() -> new UsernameNotFoundException("email-receiving user not found")))
                .setTo(userRepository.findByUsername(emailServiceModel
                                .getTo())
                        .orElseThrow(() -> new UsernameNotFoundException("email-sending user not found")));
        return emailRepository.save(email);
    }

    @Override
    public List<EmailViewModel> getAllEmailsForUser(String name) {
        Optional<List<EmailEntity>> email= emailRepository
                .getAllByTo(userRepository.findByUsername(name)
                        .orElseThrow(() -> new UsernameNotFoundException("username " + name + " not found")));
        if (email.isEmpty()) return new ArrayList<>();
        return email.get().stream()
                .map(this::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public void initUsers() {
        if (emailRepository.findAll().size() == 0) {

            UserEntity user = userRepository.findByUsername("user")
                    .orElseThrow(() -> new UsernameNotFoundException("user not initialized"));
            UserEntity debian = userRepository.findByUsername("debian")
                    .orElseThrow(() -> new UsernameNotFoundException("debian not initialized"));
            UserEntity manjaro = userRepository.findByUsername("manjaro")
                    .orElseThrow(() -> new UsernameNotFoundException("manjaro not initialized"));

            //USER SENDING
            EmailEntity userToDebian = new EmailEntity();
            userToDebian.setFrom(user)
                    .setTo(debian)
                    .setTitle("title from user")
                    .setContent("content from user")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment(null);
            emailRepository.save(userToDebian);

            EmailEntity userToManjaro = new EmailEntity();
            userToManjaro.setFrom(user)
                    .setTo(manjaro)
                    .setTitle("title from user")
                    .setContent("content from user")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("( ͡❛ ͜ʖ ͡❛)".getBytes(StandardCharsets.UTF_8));
            emailRepository.save(userToManjaro);


            //DEBIAN SENDING
            EmailEntity debianToUser= new EmailEntity();
            debianToUser.setFrom(debian)
                    .setTo(user)
                    .setTitle("title from debian")
                    .setContent("content from debian")
                    .setCreated(LocalDateTime.now())
                    .setRead(true)
                    .setAttachment(null);
            emailRepository.save(debianToUser);

            EmailEntity debianToManjaro = new EmailEntity();
            debianToManjaro.setFrom(debian)
                    .setTo(manjaro)
                    .setTitle("title from debian")
                    .setContent("content from debian")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("( ◑‿◑)ɔ┏🍟--🍔┑٩(^◡^ )".getBytes(StandardCharsets.UTF_8));
            emailRepository.save(debianToManjaro);


            //MANJARO SENDING
            EmailEntity manjaroToUser= new EmailEntity();
            manjaroToUser.setFrom(manjaro)
                    .setTo(user)
                    .setTitle("title from manjaro")
                    .setContent("content from manjaro")
                    .setCreated(LocalDateTime.now())
                    .setRead(true)
                    .setAttachment(null);
            emailRepository.save(manjaroToUser);

            EmailEntity manjaroToDebian= new EmailEntity();
            manjaroToDebian.setFrom(manjaro)
                    .setTo(debian)
                    .setTitle("title from manjaro")
                    .setContent("content from manjaro")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("(>‿◠)✌".getBytes(StandardCharsets.UTF_8));
            emailRepository.save(manjaroToDebian);
            LOGGER.info("emails initialised");
        }
    }

    private EmailViewModel toViewModel(EmailEntity r) {
        EmailViewModel email = new EmailViewModel();
        email.setDate(r.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setFrom(r.getFrom().getUsername() + "@kmai.com")
                .setRead(r.isRead())
                .setTitle(r.getTitle());
        return email;
    }
}
