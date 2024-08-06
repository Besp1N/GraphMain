package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import com.kacper.backend.device.DeviceRepository;
import com.kacper.backend.device.DeviceService;
import com.kacper.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorService
{
    private final SensorRepository sensorRepository;
    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;

    public SensorService(SensorRepository sensorRepository, DeviceService deviceService, DeviceRepository deviceRepository) {
        this.sensorRepository = sensorRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

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

    public Sensor getSensorById(Integer sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor " + sensorId.toString() + " Not found"
        ));
    }

    public Sensor deleteSensorById(Integer sensorId) {
        Sensor sensor = getSensorById(sensorId);
        sensorRepository.delete(sensor);
        return sensor;
    }
}
