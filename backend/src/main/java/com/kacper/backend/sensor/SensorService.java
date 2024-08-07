package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import com.kacper.backend.device.DeviceRepository;
import com.kacper.backend.device.DeviceService;
import com.kacper.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing sensors.
 * Handles operations related to sensors such as adding, retrieving, and deleting sensors.
 */
@Service
public class SensorService
{
    private final SensorRepository sensorRepository;
    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;

    /**
     * @param sensorRepository The repository for managing sensor entities.
     * @param deviceService The service for managing device operations.
     * @param deviceRepository The repository for managing device entities.
     */
    public SensorService(
            SensorRepository sensorRepository,
            DeviceService deviceService,
            DeviceRepository deviceRepository
    ) {
        this.sensorRepository = sensorRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Adds a sensor to a specific device.
     *
     * @param deviceId The ID of the device to which the sensor will be added.
     * @param sensorRequest The request body containing sensor information.
     * @return The {@link Device} with the newly added sensor.
     * @throws ResourceNotFoundException if the device with the given ID is not found.
     */
    @Transactional
    public Device addSensorToDevice(Integer deviceId, SensorRequest sensorRequest) {
        Device device = deviceService.getDeviceById(deviceId);

        Sensor sensor = Sensor.builder()
                .device(device)
                .sensorType(sensorRequest.sensorType())
                .sensorName(sensorRequest.sensorName())
                .build();

        device.getSensors().add(sensor);
        sensorRepository.save(sensor);
        deviceRepository.save(device);

        return device;
    }

    /**
     * Retrieves a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to retrieve.
     * @return The {@link Sensor} with the specified ID.
     * @throws ResourceNotFoundException if the sensor with the given ID is not found.
     */
    public Sensor getSensorById(Integer sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor " + sensorId.toString() + " Not found"
                ));
    }

    /**
     * Deletes a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to delete.
     * @return The {@link Sensor} that was deleted.
     * @throws ResourceNotFoundException if the sensor with the given ID is not found.
     */
    public Sensor deleteSensorById(Integer sensorId) {
        Sensor sensor = getSensorById(sensorId);
        sensorRepository.delete(sensor);
        return sensor;
    }
}
