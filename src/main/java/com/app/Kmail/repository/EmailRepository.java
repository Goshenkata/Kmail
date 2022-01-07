package com.app.Kmail.repository;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    Optional<List<EmailEntity>> getAllByTo(UserEntity user);
}
