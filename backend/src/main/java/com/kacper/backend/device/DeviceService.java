package com.kacper.backend.device;

import com.kacper.backend.exception.ResourceNotFoundException;
import com.kacper.backend.sensor.SensorRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService
{
    private final DeviceRepository deviceRepository;
    private final DevicePresentationMapper devicePresentationMapper;

    public DeviceService(
            DeviceRepository deviceRepository,
            DevicePresentationMapper devicePresentationMapper
    ) {
        this.deviceRepository = deviceRepository;
        this.devicePresentationMapper = devicePresentationMapper;
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

    public Device getDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device " + id.toString() + " Not found"));
    }

}
