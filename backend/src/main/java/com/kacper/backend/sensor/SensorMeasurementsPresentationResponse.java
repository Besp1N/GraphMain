package com.kacper.backend.sensor;

import com.kacper.backend.measurement.Measurement;

import java.util.List;

/**
 * @param id is the id of the sensor
 * @param sensorName is the name of the sensor
 * @param sensorType is the type of the sensor
 * @param unit is the unit of the sensor
 * @param measurementList is the list of measurements
 *
 * @Author Kacper Karabinowski
 */
public record SensorMeasurementsPresentationResponse(
        Integer id,
        String sensorName,
        String sensorType,
        String unit,
        List<Measurement> measurementList
) {
}
