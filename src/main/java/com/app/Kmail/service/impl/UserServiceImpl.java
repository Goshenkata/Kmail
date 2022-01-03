package com.app.Kmail.service.impl;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.UserRegistrationServiceModel;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KmailUserServiceImpl kmailUserService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, KmailUserServiceImpl kmailUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kmailUserService = kmailUserService;
    }

    @Override
    public boolean isUsernameValid(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {
        UserEntity userEntity = new UserEntity()
                .setUsername(userRegistrationServiceModel.getUsername())
                .setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()))
                .setFirstName(userRegistrationServiceModel.getFirstName())
                .setLastName(userRegistrationServiceModel.getLastName());
        userRepository.save(userEntity);

        UserDetails principal = kmailUserService.loadUserByUsername(userEntity.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, userEntity.getPassword(), principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userEntity;
    }
}
