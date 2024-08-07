package com.kacper.backend.device;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author Kacper Karabinowski
 */
@Service
public class DevicePresentationMapper implements Function<Device, DevicePresentationResponse>
{
    /**
     * @param device device to be mapped
     * @return mapped device
     */
    @Override
    public DevicePresentationResponse apply(Device device) {
        return new DevicePresentationResponse(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType()
        );
    }
}
