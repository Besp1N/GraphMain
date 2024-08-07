package com.kacper.backend.device;

import com.kacper.backend.sensor.Sensor;

public record DeviceMeasurementPresentation(
        Integer deviceId,
        String deviceName,
        String deviceType,
        Sensor sensor
) {
}
