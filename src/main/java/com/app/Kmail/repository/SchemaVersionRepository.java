package com.app.Kmail.repository;

import com.app.Kmail.model.entity.SchemaVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemaVersionRepository extends JpaRepository<SchemaVersionEntity, Long> {
    @Query("SELECT COALESCE(MAX(s.version), 0) FROM SchemaVersionEntity s")
    Integer findLatestVersion();
}
