package com.kacper.backend.device;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kacper Karabinowski
 * @version 1.0
 * Device controller class.
 */
@RestController
@RequestMapping("/api/v1/device")
public class DeviceController
{
    private final DeviceService deviceService;

    /**
     * @param deviceService is injected automatically by Spring
     */
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * @param deviceRequest is a request body
     * @return DevicePresentationResponse
     * Adds a device to the database (Admin)
     */
    // Admin endpoint
    @PostMapping("/")
    public DevicePresentationResponse addDevice(
            @Valid @RequestBody DeviceRequest deviceRequest
    ) {
        return deviceService.addDevice(deviceRequest);
    }

    /**
     * @param deviceId is a path variable
     * @return Device - whole object included sensors and measurements,
     * because of cascade type all
     */
    // Admin endpoint
    @DeleteMapping("/{deviceId}")
    public Device deleteDevice(
            @PathVariable Integer deviceId
    ) {
        return deviceService.deleteDevice(deviceId);
    }

    /**
     * @return List of DevicePresentationResponse
     * Returns all devices with basic information (device name, device type + id)
     */
    @GetMapping("/")
    public List<DevicePresentationResponse> getAllDevicesPresentationInfo() {
        return deviceService.getAllDevicesPresentationInfo();
    }

    /**
     * @param deviceId is a path variable
     * @return DeviceSensorsPresentationResponse with basic information about device
     * and sensors (sensor name, sensor type + id)
     *
     */
    // Device + sensors
    @GetMapping("/sensor/{deviceId}")
    public DeviceSensorsPresentationResponse getDeviceSensorsPresentationInfo(
            @PathVariable Integer deviceId
    ) {
        return deviceService.getDeviceSensorsPresentationInfo(deviceId);
    }

    /**
     * @param sensorId is a path variable
     * @return DeviceMeasurementPresentation with specific sensor information + measurements of sensor
     */
    // Device + ONE sensor + measurements
    @GetMapping("/measurement/{sensorId}")
    public DeviceMeasurementPresentation getDeviceSensorMeasurementPresentationInfo(
            @PathVariable Integer sensorId
    ) {
        return deviceService.getDeviceSensorMeasurementPresentationInfo(sensorId);
    }

    /**
     * @param deviceId is a path variable
     * @return Device - whole object included ALL sensors and ALL measurements of ALL sensors,
     */
    // returns whole device object (including sensors + measurements)
    @GetMapping("/{deviceId}")
    public Device getDeviceById(@PathVariable Integer deviceId) {
        return deviceService.getDeviceById(deviceId);
    }
}
