package com.kacper.backend.device;

public record DevicePresentationResponse(
        Integer id,
        String deviceName,
        String deviceType
) {
}
