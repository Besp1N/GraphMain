package com.kacper.backend.device;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DevicePresentationMapper implements Function<Device, DevicePresentationResponse>
{
    @Override
    public DevicePresentationResponse apply(Device device) {
        return new DevicePresentationResponse(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType()
        );
    }
}
