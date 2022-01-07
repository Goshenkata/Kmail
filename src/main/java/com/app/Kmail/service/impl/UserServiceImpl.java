package com.app.Kmail.service.impl;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.model.service.UserRegistrationServiceModel;
import com.app.Kmail.repository.UserRepository;
import com.app.Kmail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KmailUserServiceImpl kmailUserService;
    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
        LOGGER.info("{} registered and logged in", userEntity.getUsername());
        return userEntity;
    }

    @Override
    public void initUsers() {
        //username: user
        //password: user
        if (userRepository.findAll().size() <= 3) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setFirstName("user");
            user.setLastName("userov");
            user.setPassword(passwordEncoder.encode("user"));
            userRepository.save(user);
            //username: debian
            //password: debian
            UserEntity debian = new UserEntity();
            debian.setUsername("debian");
            debian.setFirstName("Debra");
            debian.setLastName("Ian");
            debian.setPassword(passwordEncoder.encode("debian"));
            userRepository.save(debian);
            //username: manjaro
            //password: manjaro
            UserEntity manjaro = new UserEntity();
            manjaro.setUsername("manjaro");
            manjaro.setFirstName("manjaro");
            manjaro.setLastName("manjarov");
            manjaro.setPassword(passwordEncoder.encode("manjaro"));
            userRepository.save(manjaro);

            LOGGER.info("users initialized: {}, {} and {}", user.getUsername(), debian.getUsername(), manjaro.getUsername());
        }
    }

    @Override
    public String removeAddress(String email) {
        if (!email.endsWith("@kmail.com")) {
            throw new IllegalArgumentException("The email does not end with @kmail.com and thus the address can't be removed");
        }
        return email.substring(0, email.length()-10);
    }

    @Override
    public Optional<UserEntity> findByUsernameWithAddress(String usernameWithAdress) {
        String username = removeAddress(usernameWithAdress);
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

}
