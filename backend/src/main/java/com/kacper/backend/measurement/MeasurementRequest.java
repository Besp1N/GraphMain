package com.kacper.backend.measurement;

import java.time.LocalDateTime;

public record MeasurementRequest(
        Double value,
        String unit,
        LocalDateTime timestamp
) {
}
