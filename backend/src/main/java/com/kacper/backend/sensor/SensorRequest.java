package com.kacper.backend.sensor;

public record SensorRequest(
        String sensorName,
        String sensorType
) {
}
