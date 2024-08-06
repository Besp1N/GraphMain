package com.kacper.backend.device;

import com.kacper.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService
{
    private final DeviceRepository deviceRepository;

    public DeviceService(
            DeviceRepository deviceRepository
    ) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceCreationResponse addDevice(DeviceRequest deviceRequest) {
        Device device =  Device.builder()
                .deviceName(deviceRequest.deviceName())
                .deviceType(deviceRequest.deviceType())
                .build();

        Device savedDevice = deviceRepository.save(device);

        return new DeviceCreationResponse(
                savedDevice.getId(),
                savedDevice.getDeviceName(),
                savedDevice.getDeviceType()
        );
    }

    public List<Device> getAllDevicesInfo() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device " + id.toString() + " Not found"));
    }
}
