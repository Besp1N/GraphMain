package com.kacper.backend.notification;

import com.kacper.backend.user.User;
import com.kacper.backend.user.UserRepository;
import com.kacper.backend.user_notification.UserNotification;
import com.kacper.backend.user_notification.UserNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService
{
    private final UserRepository userRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(
            UserRepository userRepository,
            UserNotificationRepository userNotificationRepository,
            NotificationRepository notificationRepository
    ) {
        this.userRepository = userRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.notificationRepository = notificationRepository;
    }

    public void addUserNotification(NotificationDTO notificationDTO) {
        userNotificationRepository.addUserNotifications(notificationDTO.id());
    }
}
