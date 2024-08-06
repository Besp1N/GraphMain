package com.kacper.backend.device;

import jakarta.validation.constraints.NotBlank;

public record DeviceRequest(
        @NotBlank(message = "Device name cannot be blank")
        String deviceName,

        @NotBlank(message = "Device type cannot be blank")
        String deviceType
) {
}
