package com.kacper.backend.sensor;

import jakarta.validation.constraints.NotBlank;

/**
 * @param sensorName is the name of the sensor with NotBlank validation
 * @param sensorType is the type of the sensor with NotBlank validation
 *
 * @author Kacper Karabinowski
 */
public record SensorRequest(
        @NotBlank(message = "Sensor name can not be blank")
        String sensorName,

        @NotBlank(message = "Sensor type can not be blank")
        String sensorType
) {
}
