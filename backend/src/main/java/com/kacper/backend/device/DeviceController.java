package com.kacper.backend.device;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public DeviceCreationResponse addDevice(
            @Valid @RequestBody DeviceRequest deviceRequest
    ) {
        return deviceService.addDevice(deviceRequest);
    }

    @GetMapping("/")
    public List<Device> getAllDevicesInfo() {
        return deviceService.getAllDevicesInfo();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Integer id) {
        return deviceService.getDeviceById(id);
    }
}
