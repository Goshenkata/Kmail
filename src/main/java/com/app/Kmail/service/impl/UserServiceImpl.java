package com.app.Kmail.service.impl;

import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.UserService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUsernameValid(String username) {
        Pattern pattern = Pattern.compile("/^[a-zA-Z0-9]+$/gm");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
