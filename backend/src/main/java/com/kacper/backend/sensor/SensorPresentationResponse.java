package com.kacper.backend.sensor;

import lombok.Builder;
import lombok.Data;

public record SensorPresentationResponse(
        Integer id,
        String sensorName,
        String sensorType
) {
}
