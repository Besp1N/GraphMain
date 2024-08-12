package com.kacper.backend.sensor;

import com.kacper.backend.measurement.Measurement;

import java.util.List;

public record SensorMeasurementsPresentationResponse(
        Integer id,
        String sensorName,
        String sensorType,
        String unit,
        List<Measurement> measurementList
) {
}
