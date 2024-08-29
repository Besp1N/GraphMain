package com.kacper.backend.notification;

import com.kacper.backend.measurement.Measurement;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Integer id,
        String message,
        String type,
        LocalDateTime created_at,
        Integer device_id,
        Integer sensor_id,
        Measurement measurement
) {
}
