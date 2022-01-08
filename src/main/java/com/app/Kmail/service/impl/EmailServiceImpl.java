package com.app.Kmail.service.impl;

import com.app.Kmail.exceptions.EmailNotFoundException;
import com.app.Kmail.exceptions.FileNotFoundException;
import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.model.view.EmailViewModel;
import com.app.Kmail.model.view.InboxViewModel;
import com.app.Kmail.repository.EmailRepository;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                        .orElseThrow(() -> new UsernameNotFoundException("email-sending user not found")))
                .setAttachmentName(emailServiceModel.getAttachmentName());
        return emailRepository.save(email);
    }

    @Override
    public List<InboxViewModel> getAllEmailsForUser(String name) {
        Optional<List<EmailEntity>> email= emailRepository
                .getAllByTo(userRepository.findByUsername(name)
                        .orElseThrow(() -> new UsernameNotFoundException("username " + name + " not found")));
        if (email.isEmpty()) return new ArrayList<>();
        return email.get().stream()
                .map(this::toInboxViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public void initEmail() {
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
                    .setAttachment(null)
                    .setAttachmentName(null);
            emailRepository.save(userToDebian);

            EmailEntity userToManjaro = new EmailEntity();
            userToManjaro.setFrom(user)
                    .setTo(manjaro)
                    .setTitle("title from user")
                    .setContent("content from user")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("( ͡❛ ͜ʖ ͡❛)".getBytes(StandardCharsets.UTF_8))
                    .setAttachmentName("important.txt");
            emailRepository.save(userToManjaro);


            //DEBIAN SENDING
            EmailEntity debianToUser= new EmailEntity();
            debianToUser.setFrom(debian)
                    .setTo(user)
                    .setTitle("title from debian")
                    .setContent("content from debian")
                    .setCreated(LocalDateTime.now())
                    .setRead(true)
                    .setAttachment(null)
                    .setAttachmentName(null);
            emailRepository.save(debianToUser);

            EmailEntity debianToManjaro = new EmailEntity();
            debianToManjaro.setFrom(debian)
                    .setTo(manjaro)
                    .setTitle("title from debian")
                    .setContent("content from debian")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("( ◑‿◑)ɔ┏🍟--🍔┑٩(^◡^ )".getBytes(StandardCharsets.UTF_8))
                    .setAttachmentName("important.txt");
            emailRepository.save(debianToManjaro);


            //MANJARO SENDING
            EmailEntity manjaroToUser= new EmailEntity();
            manjaroToUser.setFrom(manjaro)
                    .setTo(user)
                    .setTitle("title from manjaro")
                    .setContent("content from manjaro")
                    .setCreated(LocalDateTime.now())
                    .setRead(true)
                    .setAttachment(null)
                    .setAttachmentName(null);
            emailRepository.save(manjaroToUser);

            EmailEntity manjaroToDebian= new EmailEntity();
            manjaroToDebian.setFrom(manjaro)
                    .setTo(debian)
                    .setTitle("title from manjaro")
                    .setContent("content from manjaro")
                    .setCreated(LocalDateTime.now())
                    .setRead(false)
                    .setAttachment("(>‿◠)✌".getBytes(StandardCharsets.UTF_8))
                    .setAttachmentName("important.txt");
            emailRepository.save(manjaroToDebian);
            LOGGER.info("emails initialised");
        }
    }

    @Override
    public List<InboxViewModel> getAllEmailsFromUser(String name) {
        Optional<List<EmailEntity>> emails = emailRepository.getAllByFrom(userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("username " + name + " not found.")));
        if (emails.isEmpty()) {
            return new ArrayList<>();
        } else {
            return emails.get().stream()
                    .map(this::toSentViewModel)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EmailViewModel getEmailViewModel(Long id) {
        return toEmailViewModel(emailRepository.findById(id)
                .orElseThrow(() -> new EmailNotFoundException("email with id " + id + " not found")));
    }

    @Override
    public EmailEntity emailSeen(Long id) {
        EmailEntity email = emailRepository.getById(id);
        email.setRead(true);
        emailRepository.save(email);
        return email;
    }

    @Override
    public FileSystemResource downloadAttachment(Long id) {
        EmailEntity email = emailRepository.getById(id);

        String prefix = email.getAttachmentName().split("\\.")[0];
        String suffix = "." + email.getAttachmentName().split("\\.")[1];
        Optional<File> file = Optional.empty();

        FileNotFoundException fileNotFoundException = new FileNotFoundException("error processing attachment of email with id: " + id);
        try {
            file = Optional.of(File.createTempFile(prefix, suffix));
            FileOutputStream fileOutputStream = new FileOutputStream(file
                    .orElseThrow(() -> fileNotFoundException));

            fileOutputStream.write(email.getAttachment());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FileSystemResource(file
                .orElseThrow(() -> fileNotFoundException));

    }

    private EmailViewModel toEmailViewModel(EmailEntity emailEntity) {
        EmailViewModel emailViewModel = new EmailViewModel();
        emailViewModel.setFrom(emailEntity.getFrom().getUsername())
                .setTo(emailEntity.getTo().getUsername())
                .setContent(emailEntity.getContent())
                .setDate(emailEntity.getCreated()
                        .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setTitle(emailEntity.getTitle())
                .setHasAttachment(!(emailEntity.getAttachmentName() == null
                        || emailEntity.getAttachmentName().isEmpty()))
                .setDownloadFileLink("/emails/" + emailEntity.getId() + "/download")
                .setAttachmentName(emailEntity.getAttachmentName());
        return emailViewModel;
    }

    private InboxViewModel toSentViewModel(EmailEntity e) {
        InboxViewModel email = new InboxViewModel();
        email.setTitle(e.getTitle())
                .setRead(e.isRead())
                .setFrom(e.getTo().getUsername() + "@kmail.com")
                .setDate(e.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setId(e.getId());
        return email;
    }

    private InboxViewModel toInboxViewModel(EmailEntity r) {
        InboxViewModel email = new InboxViewModel();
        email.setDate(r.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setFrom(r.getFrom().getUsername() + "@kmai.com")
                .setRead(r.isRead())
                .setTitle(r.getTitle())
                .setId(r.getId());
        return email;
    }
}
