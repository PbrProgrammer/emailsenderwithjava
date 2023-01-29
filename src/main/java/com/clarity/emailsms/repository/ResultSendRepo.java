package com.clarity.emailsms.repository;

import com.clarity.emailsms.entity.ResultSendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultSendRepo extends JpaRepository<ResultSendEntity, Long> {
}
