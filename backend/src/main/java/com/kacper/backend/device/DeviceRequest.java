package com.kacper.backend.device;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Kacper Karabinowski
 *
 * @param deviceName name of the device with NotBlank validation
 * @param deviceType type of the device with NotBlank validation
 */
public record DeviceRequest(
        @NotBlank(message = "Device name cannot be blank")
        String deviceName,

        @NotBlank(message = "Device type cannot be blank")
        String deviceType
) {
}
