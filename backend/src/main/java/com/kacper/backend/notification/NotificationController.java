package com.kacper.backend.notification;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Notification controller class
 *
 * @Author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController
{
    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{numPage}")
    public List<NotificationResponse> getNotifications(@PathVariable Integer numPage) {
        return notificationService.getNotifications(numPage);
    }
}
