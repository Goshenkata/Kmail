package com.app.Kmail.service.impl;

import com.app.Kmail.model.entity.UserEntity;
import com.app.Kmail.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KmailUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public KmailUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found! :("));
        //our service does not require different authorities insofar
        return new User(userEntity.getUsername(),userEntity.getPassword(), new ArrayList<>());
    }
}
