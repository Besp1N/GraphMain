package com.kacper.backend.notification;

import lombok.Builder;

@Builder
public record NotificationMapper(
        Integer id,
        String message,
        String type,
        String created_at,
        Integer measurement_id
) {
}
