package com.app.Kmail.service;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.model.view.EmailViewModel;

import java.util.List;
import java.util.Optional;

public interface EmailService {
    EmailEntity save(EmailServiceModel emailServiceModel);

    //will get all incoming emails for the given user
    List<EmailViewModel> getAllEmailsForUser(String name);

    //every user send 2 email to every other user
    void initUsers();
}
