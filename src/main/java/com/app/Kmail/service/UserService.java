package com.app.Kmail.service;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.UserRegistrationServiceModel;
import com.app.Kmail.model.view.LoggedInUserIndexViewModel;

import java.util.Optional;

public interface UserService {

    //this will test if the username does not contain special characters
    boolean isUsernameValid(String username);

    boolean isUsernameTaken(String username);

    UserEntity registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    //initializes 3 users: user, debian, manajaro, passwords are he same as the names
    void initUsers();

    //this will remove the address i.e user@kmail.com -> user
    String removeAddress(String email);

    //this will remove the address and find the UserEntity by the username i.e user@kmail.com -> (UserEntity) user
    Optional<UserEntity> findByUsernameWithAddress(String usernameWithAdress);

    void deleteAll();

    LoggedInUserIndexViewModel getData(String name);
}
