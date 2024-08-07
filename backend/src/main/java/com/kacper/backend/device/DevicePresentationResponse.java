package com.kacper.backend.device;

/**
 * @author Kacper Karabinowski
 *
 * @param id is the id of the device
 * @param deviceName is the name of the device
 * @param deviceType is the type of the device
 */
public record DevicePresentationResponse(
        Integer id,
        String deviceName,
        String deviceType
) {
}
