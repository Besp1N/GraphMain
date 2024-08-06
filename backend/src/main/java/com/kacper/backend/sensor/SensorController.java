package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{deviceId}")
    public Device addSensorToDevice(@PathVariable Integer deviceId, @RequestBody SensorRequest sensorRequest) {
        return sensorService.addSensorToDevice(deviceId, sensorRequest);
    }
}
