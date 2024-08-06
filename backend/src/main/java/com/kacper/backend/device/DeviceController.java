package com.kacper.backend.device;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device")
public class DeviceController
{
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Admin endpoint
    @PostMapping("/")
    public DevicePresentationResponse addDevice(
            @Valid @RequestBody DeviceRequest deviceRequest
    ) {
        return deviceService.addDevice(deviceRequest);
    }

    @GetMapping("/")
    public List<DevicePresentationResponse> getAllDevicesPresentationInfo() {
        return deviceService.getAllDevicesPresentationInfo();
    }

    @GetMapping("/info/{deviceId}")
    public DeviceSensorsPresentationResponse getDeviceSensorsPresentationInfo(
            @PathVariable Integer deviceId
    ) {
        return deviceService.getDeviceSensorsPresentationInfo(deviceId);
    }

    // returns whole device object (including sensors + measurements)
    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Integer id) {
        return deviceService.getDeviceById(id);
    }
}
