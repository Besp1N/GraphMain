package com.kacper.backend.device;

import com.kacper.backend.sensor.SensorPresentationResponse;

import java.util.List;

/**
 * @author Kacper Karabinowski
 * Response for the API including the device and its sensors
 *
 * @param id is the id of the device
 * @param deviceName is the name of the device
 * @param deviceType is the type of the device
 * @param sensors is the list of sensors of the device mapped to SensorPresentationResponse
 */
public record DeviceSensorsPresentationResponse(
        Integer id,
        String deviceName,
        String deviceType,
        List<SensorPresentationResponse> sensors
) {
}
