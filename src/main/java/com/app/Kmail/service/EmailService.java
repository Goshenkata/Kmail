package com.app.Kmail.service;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.EmailServiceModel;

public interface EmailService {
    EmailEntity save(EmailServiceModel emailServiceModel);
}
