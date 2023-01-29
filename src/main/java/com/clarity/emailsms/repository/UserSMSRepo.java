package com.clarity.emailsms.repository;

import com.clarity.emailsms.entity.UserSMSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSMSRepo extends JpaRepository<UserSMSEntity, Long> {
}
