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
        // Koniecznie kiedys napisac query bo to za duze kongo z tymi userami
        List<User> users = userRepository.findAll();
        for (User user : users) {
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(notificationRepository.findById(notificationDTO.id()).orElseThrow())
                    .seen(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }
    }

    private void sendMailNotification() {
        // TODO: Implement albo uzyc tej funkcji z mail package
    }
}
