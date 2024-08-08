package com.kacper.backend.device;

import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorMeasurementsPresentationResponse;

/**
 * Used for response in the API
 *
 * @author Kacper Karabinowski
 *
 * @param deviceId id of the device
 * @param deviceName name of the device
 * @param deviceType type of the device
 * @param sensor sensor of the device
 */
public record DeviceMeasurementPresentation(
        Integer deviceId,
        String deviceName,
        String deviceType,
        Integer totalPages,
        SensorMeasurementsPresentationResponse sensor
) {
}
