package com.app.Kmail.service;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.UserRegistrationServiceModel;

public interface UserService {

    //this will test if the username does not contain special characters
    boolean isUsernameValid(String username);

    boolean isUsernameTaken(String username);

    UserEntity registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    void initUsers();
}
