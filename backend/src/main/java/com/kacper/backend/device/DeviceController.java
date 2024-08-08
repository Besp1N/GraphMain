package com.kacper.backend.device;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Device controller class.
 * Handles all requests related to devices.
 *
 * @author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/v1/device")
public class DeviceController
{
    private final DeviceService deviceService;

    /**
     * @param deviceService is injected automatically by Spring
     * @see DeviceService
     */
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Adds a device to the database (Admin)
     *
     * @param deviceRequest is a request body
     * @return DevicePresentationResponse
     */
    // Admin endpoint
    @PostMapping("/")
    public DevicePresentationResponse addDevice(
            @Valid @RequestBody DeviceRequest deviceRequest
    ) {
        return deviceService.addDevice(deviceRequest);
    }

    /**
     * Deletes a device from the database (Admin)
     *
     * @param deviceId is a path variable
     * @return Device - whole object included sensors and measurements (CascadeType.ALL)
     */
    // Admin endpoint
    @DeleteMapping("/{deviceId}")
    public Device deleteDevice(
            @PathVariable Integer deviceId
    ) {
        return deviceService.deleteDevice(deviceId);
    }

    /**
     * @return List of DevicePresentationResponse with basic information (device name, device type + id)
     */
    @GetMapping("/")
    public List<DevicePresentationResponse> getAllDevicesPresentationInfo() {
        return deviceService.getAllDevicesPresentationInfo();
    }

    /**
     * @param deviceId is a path variable
     * @return DeviceSensorsPresentationResponse with basic information about device
     * and sensors (sensor name, sensor type + id)
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
            @PathVariable Integer sensorId,
            @RequestParam Integer numPage,
            @RequestParam Integer from,
            @RequestParam Integer to
    ) {
        return deviceService.getDeviceSensorMeasurementPresentationInfo(sensorId, numPage, from, to);
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
