package com.kacper.backend.notification;

import com.kacper.backend.measurement.Measurement;
import lombok.Builder;

@Builder
public record NotificationResponse(
        Integer id,
        String message,
        String type,
        String createdAt,
        Measurement measurement
) {
}
