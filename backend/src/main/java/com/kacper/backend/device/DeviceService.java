package com.kacper.backend.device;

import com.kacper.backend.exception.ResourceNotFoundException;
import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.measurement.MeasurementRepository;
import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorMeasurementsPresentationResponse;
import com.kacper.backend.sensor.SensorPresentationResponse;
import com.kacper.backend.sensor.SensorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for the device
 *
 * @author Kacper Karabinowski
 */
@Service
public class DeviceService
{
    private final DeviceRepository deviceRepository;
    private final DevicePresentationMapper devicePresentationMapper;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;

    /**
     * Injected by the constructor
     *
     * @param deviceRepository is the repository for the device
     * @param devicePresentationMapper is the mapper for the device
     * @param sensorRepository is the repository for the sensor
     */
    public DeviceService(
            DeviceRepository deviceRepository,
            DevicePresentationMapper devicePresentationMapper,
            SensorRepository sensorRepository,
            MeasurementRepository measurementRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.devicePresentationMapper = devicePresentationMapper;
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;
    }

    /**
     * @param deviceRequest is the request for the device
     * @return the mapped response for the device
     */
    public DevicePresentationResponse addDevice(DeviceRequest deviceRequest) {
        Device device =  Device.builder()
                .deviceName(deviceRequest.deviceName())
                .deviceType(deviceRequest.deviceType())
                .build();

        Device savedDevice = deviceRepository.save(device);

        return devicePresentationMapper.apply(savedDevice);
    }

    /**
     * @return the list of all devices mapped to the response
     */
    public List<DevicePresentationResponse> getAllDevicesPresentationInfo() {
        return deviceRepository.findAll().stream()
                .map(devicePresentationMapper)
                .collect(Collectors.toList());
    }

    /**
     * @param id is the id of the device
     * @return the device with the given id
     */
    // for debugging or sth ( make private in future )
    public Device getDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device " + id.toString() + " Not found"));
    }

    /**
     * @param deviceId is the id of the device
     * @return the device with the given id and its sensors mapped to the response
     */
    // Change it for SensorPresentationMapper
    public DeviceSensorsPresentationResponse getDeviceSensorsPresentationInfo(Integer deviceId) {
        Device device = getDeviceById(deviceId);
        return new DeviceSensorsPresentationResponse(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType(),
                device.getSensors().stream()
                        .map(sensor -> new SensorPresentationResponse(
                                sensor.getId(),
                                sensor.getSensorName(),
                                sensor.getSensorType(),
                                sensor.getUnit()
                        ))
                        .collect(Collectors.toList())
        );
    }

    /**
     * @param deviceId is the id of the device
     * @return the device with the given id and its sensors and its measurements
     */
    public Device deleteDevice(Integer deviceId) {
        Device device = getDeviceById(deviceId);
        deviceRepository.delete(device);
        return device;
    }

    /**
     * @param sensorId is the id of the sensor
     * @return the device with the given sensor and its measurements mapped to response
     */
    public DeviceMeasurementPresentation getDeviceSensorMeasurementPresentationInfo(
            Integer sensorId,
            Integer numPage,
            Integer from,
            Integer to
    ) {
        Sensor sensor = getSensorById(sensorId);
        Device device = sensor.getDevice();

        // Convert from seconds to LocalDateTime
        LocalDateTime fromTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault());
        LocalDateTime toTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault());

        // Second argument is page size, change it if needed
        PageRequest pageable = PageRequest.of(numPage, 15);
        Page<Measurement> measurementsPage = measurementRepository
                .findAllBySensorIdAndTimestampBetween(sensorId, fromTime, toTime, pageable);
        List<Measurement> measurements = measurementsPage.getContent();
        int totalPages = measurementsPage.getTotalPages();


        return new DeviceMeasurementPresentation(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType(),
                totalPages,
                new SensorMeasurementsPresentationResponse(
                        sensor.getId(),
                        sensor.getSensorName(),
                        sensor.getSensorType(),
                        sensor.getUnit(),
                        measurements
                )
        );
    }

    private Sensor getSensorById(Integer sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor " + sensorId.toString() + " Not found"));
    }

    /**
     * @param sensorId is the id of the sensor
     * @param max is the maximum number of measurements
     * @param from is the start time
     * @param to is the end time
     * @return the device with the given sensor and its measurements mapped to response
     */
    public DeviceMeasurementPresentation getDeviceSensorMeasurementPresentationInfoGraph(
            Integer sensorId,
            Integer max,
            Integer from,
            Integer to) {
        Sensor sensor = getSensorById(sensorId);
        Device device = sensor.getDevice();

        LocalDateTime fromTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault());
        LocalDateTime toTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault());

        long totalMeasurements = measurementRepository.countBySensorIdAndTimestampBetween(sensorId, fromTime, toTime);
        int interval = (int) Math.ceil((double) totalMeasurements / max);

        List<Measurement> filteredMeasurements = measurementRepository.findMeasurementsBySensorIdWithIntervalAndTimestampBetween(sensorId, interval, fromTime, toTime);

        return new DeviceMeasurementPresentation(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType(),
                1, // Kacper Karabinowski - Since we are not paginating, we can set totalPages to 1
                new SensorMeasurementsPresentationResponse(
                        sensor.getId(),
                        sensor.getSensorName(),
                        sensor.getSensorType(),
                        sensor.getUnit(),
                        filteredMeasurements
                )
        );
    }
}
