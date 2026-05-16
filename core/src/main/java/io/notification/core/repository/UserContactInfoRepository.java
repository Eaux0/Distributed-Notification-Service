package io.notification.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.notification.core.model.UserContactInfo;

public interface UserContactInfoRepository extends JpaRepository<UserContactInfo, Long> {

    UserContactInfo findByUserId(Long userId);
}
