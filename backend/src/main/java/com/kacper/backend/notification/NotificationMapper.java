package com.kacper.backend.notification;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationMapper(
        Integer id,
        String message,
        String type,
        LocalDateTime created_at,
        Integer measurement_id
) {
}
