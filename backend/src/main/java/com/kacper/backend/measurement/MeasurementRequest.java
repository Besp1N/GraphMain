package com.kacper.backend.measurement;

import java.time.LocalDateTime;

/**
 * @param value is the value of the measurement
 * @param timestamp is the timestamp of the measurement
 *
 * @Author Kacper Karabinowski
 */
public record MeasurementRequest(
        Double value,
        LocalDateTime timestamp
) {
}
