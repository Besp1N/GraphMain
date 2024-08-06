package com.kacper.backend.device;

public record DeviceCreationResponse(
        Integer id,
        String deviceName,
        String deviceType
) {
}
