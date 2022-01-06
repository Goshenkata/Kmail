package com.app.Kmail.service.impl;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.repository.EmailRepository;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.EmailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

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
}
