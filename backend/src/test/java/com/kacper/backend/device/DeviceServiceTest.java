package com.kacper.backend.device;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DevicePresentationMapper devicePresentationMapper;

    @InjectMocks
    private DeviceService deviceService;

    private List<Device> devices;
    private List<DevicePresentationResponse> devicePresentationResponses;

    @BeforeEach
    void setUp() {
        devices = Stream.of(
                new Device(1, "Device1", "Type1", new ArrayList<>()),
                new Device(2, "Device2", "Type2", new ArrayList<>())
        ).collect(Collectors.toList());

        devicePresentationResponses = devices.stream()
                .map(device -> new DevicePresentationResponse(device.getId(), device.getDeviceName(), device.getDeviceType()))
                .collect(Collectors.toList());
    }

    @Test
    void testGetAllDevicesPresentationInfo() {
        // Given
        when(deviceRepository.findAll()).thenReturn(devices);
        when(devicePresentationMapper.apply(any(Device.class))).thenAnswer(invocation -> {
            Device device = invocation.getArgument(0);
            return new DevicePresentationResponse(device.getId(), device.getDeviceName(), device.getDeviceType());
        });

        // When
        List<DevicePresentationResponse> result = deviceService.getAllDevicesPresentationInfo();

        // Then
        assertEquals(devicePresentationResponses.size(), result.size());

        result.forEach(devicePresentationResponse -> {
            assertTrue(devicePresentationResponses.contains(devicePresentationResponse));
        });

        verify(deviceRepository, times(1)).findAll();
        verify(devicePresentationMapper, times(devices.size())).apply(any(Device.class));
    }
}
