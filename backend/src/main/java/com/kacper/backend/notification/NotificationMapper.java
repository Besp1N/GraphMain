package com.kacper.backend.notification;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationMapper(
        Integer id,
        String message,
        String type,
        String created_at,
        Integer measurement_id
) {
}
