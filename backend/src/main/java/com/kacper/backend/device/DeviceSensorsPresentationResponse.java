package com.kacper.backend.device;

import com.kacper.backend.sensor.SensorPresentationResponse;

import java.util.List;

public record DeviceSensorsPresentationResponse(
        Integer id,
        String deviceName,
        String deviceType,
        List<SensorPresentationResponse> sensors
) {
}
