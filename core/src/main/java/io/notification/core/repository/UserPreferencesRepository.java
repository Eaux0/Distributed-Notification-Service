package io.notification.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.notification.core.model.UserPreferences;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    UserPreferences findByUserId(Long userId);
}