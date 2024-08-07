package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing sensors within devices.
 * Handles HTTP requests related to sensors.
 *
 * @author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
    private final SensorService sensorService;

    /**
     * @param sensorService The service for managing sensor operations.
     */
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * Adds a sensor to a specific device.
     *
     * @param deviceId The ID of the device to which the sensor will be added.
     * @param sensorRequest The request body containing sensor information.
     * @return The updated {@link Device} with the newly added sensor.
     */
    @PostMapping("/{deviceId}")
    public Device addSensorToDevice(
            @PathVariable Integer deviceId,
            @Valid @RequestBody SensorRequest sensorRequest
    ) {
        return sensorService.addSensorToDevice(deviceId, sensorRequest);
    }

    /**
     * Retrieves a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to retrieve.
     * @return The {@link Sensor} with the specified ID.
     */
    @GetMapping("/{sensorId}")
    public Sensor getSensorById(
            @PathVariable Integer sensorId
    ) {
        return sensorService.getSensorById(sensorId);
    }

    /**
     * Deletes a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to delete.
     * @return The {@link Sensor} that was deleted.
     */
    @DeleteMapping("/{sensorId}")
    public Sensor deleteSensorById(@PathVariable Integer sensorId) {
        return sensorService.deleteSensorById(sensorId);
    }
}
