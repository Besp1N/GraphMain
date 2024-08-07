package com.kacper.backend.device;

import com.kacper.backend.exception.ResourceNotFoundException;
import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorPresentationResponse;
import com.kacper.backend.sensor.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService
{
    private final DeviceRepository deviceRepository;
    private final DevicePresentationMapper devicePresentationMapper;
    private final SensorRepository sensorRepository;

    public DeviceService(
            DeviceRepository deviceRepository,
            DevicePresentationMapper devicePresentationMapper,
            SensorRepository sensorRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.devicePresentationMapper = devicePresentationMapper;
        this.sensorRepository = sensorRepository;
    }

    public DevicePresentationResponse addDevice(DeviceRequest deviceRequest) {
        Device device =  Device.builder()
                .deviceName(deviceRequest.deviceName())
                .deviceType(deviceRequest.deviceType())
                .build();

        Device savedDevice = deviceRepository.save(device);

        return devicePresentationMapper.apply(savedDevice);
    }

    public List<DevicePresentationResponse> getAllDevicesPresentationInfo() {
        return deviceRepository.findAll().stream()
                .map(devicePresentationMapper)
                .collect(Collectors.toList());
    }

    // for debugging or sth ( make private in future )
    public Device getDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device " + id.toString() + " Not found"));
    }

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
                                sensor.getSensorType()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public Device deleteDevice(Integer deviceId) {
        Device device = getDeviceById(deviceId);
        deviceRepository.delete(device);
        return device;
    }

    public DeviceMeasurementPresentation getDeviceSensorMeasurementPresentationInfo(
            Integer sensorId
    ) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor " + sensorId.toString() + " Not found"));

        Device device = sensor.getDevice();

        return new DeviceMeasurementPresentation(
                device.getId(),
                device.getDeviceName(),
                device.getDeviceType(),
                sensor
        );
    }
}
