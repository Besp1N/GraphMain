package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import jakarta.validation.Valid;
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
    public Device addSensorToDevice(
            @PathVariable Integer deviceId,
            @Valid @RequestBody SensorRequest sensorRequest
    ) {
        return sensorService.addSensorToDevice(deviceId, sensorRequest);
    }

    // optional function
    @GetMapping("/{sensorId}")
    public Sensor getSensorById(
            @PathVariable Integer sensorId
    ) {
        return sensorService.getSensorById(sensorId);
    }

    @DeleteMapping("/{sensorId}")
    public Sensor deleteSensorById(@PathVariable Integer sensorId) {
        return sensorService.deleteSensorById(sensorId);

    }
}
