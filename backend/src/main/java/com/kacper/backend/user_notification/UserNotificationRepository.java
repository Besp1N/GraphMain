package com.kacper.backend.user_notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer>
{
}
