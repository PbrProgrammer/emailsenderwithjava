package com.clarity.emailsms.repository;

import com.clarity.emailsms.entity.UsersEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersEmailRepo extends JpaRepository<UsersEmailEntity, Long> {

    List<UsersEmailEntity> findAllByUsername(String user);

    void deleteAllByUsername(String user);
}