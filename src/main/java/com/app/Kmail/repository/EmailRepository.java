package com.app.Kmail.repository;

import com.app.Kmail.model.entity.EmailEntity;
import com.app.Kmail.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}
