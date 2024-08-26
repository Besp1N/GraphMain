package com.kacper.backend.notification;

import java.time.LocalDateTime;

public record NotificationDTO(
        Integer id,
        String type,
        String message,
        LocalDateTime created_at,
        Integer measurement_id
) {
}
