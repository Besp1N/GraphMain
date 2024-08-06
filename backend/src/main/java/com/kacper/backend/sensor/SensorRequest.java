package com.kacper.backend.sensor;

import jakarta.validation.constraints.NotBlank;

public record SensorRequest(
        @NotBlank(message = "Sensor name can not be blank")
        String sensorName,

        @NotBlank(message = "Sensor type can not be blank")
        String sensorType
) {
}
