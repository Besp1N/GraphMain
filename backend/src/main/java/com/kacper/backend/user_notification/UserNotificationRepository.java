package com.kacper.backend.user_notification;

import com.kacper.backend.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer>
{
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_notifications " +
            "(user_id, notification_id, seen) " +
            "SELECT u.id, :notificationId, false " +
            "FROM users u", nativeQuery = true)
    void addUserNotifications(@Param("notificationId") Integer notificationId);
}
