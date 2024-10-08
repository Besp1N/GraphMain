package com.kacper.backend.notification;

import lombok.Builder;


/**
 * @param id is the id of the notification
 * @param message is the message of the notification
 * @param type is the type of the notification
 * @param created_at is the creation date of the notification
 * @param device_id is the id of the device
 * @Author Kacper Karabinowski
 */
@Builder
public record NotificationMapper(
        Integer id,
        String message,
        String type,
        String created_at,
        Integer device_id
) {
}
