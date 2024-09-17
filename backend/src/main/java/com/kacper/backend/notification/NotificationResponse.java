package com.kacper.backend.notification;

import com.kacper.backend.measurement.Measurement;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @param id is the id of the notification
 * @param message is the message of the notification
 * @param type is the type of the notification
 * @param created_at is the creation date of the notification
 * @param device_id is the id of the device
 * @param totalPages is the total number of pages
 *
 * @Author Kacper Karabinowski
 */
@Builder
public record NotificationResponse(
        Integer id,
        String message,
        String type,
        LocalDateTime created_at,
        Integer device_id,
        Integer totalPages
) {
}
