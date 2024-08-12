package com.kacper.backend.sensor;

/**
 * @param id is the id of the sensor
 * @param sensorName is the name of the sensor
 * @param sensorType is the type of the sensor
 *
 * @author Kacper Karabinowski
 */
public record SensorPresentationResponse(
        Integer id,
        String sensorName,
        String sensorType,
        String unit
) {
}
