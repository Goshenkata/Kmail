package com.app.Kmail.service;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.model.view.EmailViewModel;
import com.app.Kmail.model.view.InboxViewModel;
import org.springframework.core.io.FileSystemResource;

import java.security.Principal;
import java.util.List;

public interface EmailService {
    EmailEntity save(EmailServiceModel emailServiceModel);

    //will get all incoming emails for the given user
    List<InboxViewModel> getAllEmailsForUser(String name);

    //every user send 2 email to every other user
    void initEmail();

    List<InboxViewModel> getAllEmailsFromUser(String name);

    EmailViewModel getEmailViewModel(Long id, Principal principal);

    EmailEntity emailSeen(Long id);

    //this will turn the bytes in the db to a file available for download
    FileSystemResource downloadAttachment(Long id);

    boolean canViewEmail(Long id, String username);
}
