package com.kacper.backend.device;

import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.measurement.MeasurementRepository;
import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for the DeviceService class
 *
 * @Author Sabina Kubiyeva
 */
@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DevicePresentationMapper devicePresentationMapper;

    @InjectMocks
    private DeviceService deviceService;
    private Device device;
    private Measurement measurement;
    private DeviceRequest deviceRequest;

    /**
     * Set up the test environment
     */
    @BeforeEach
    void setUp() {
        device = Device.builder().id(1).deviceName("Device1").deviceType("Type1").build();
        Sensor sensor = Sensor.builder().id(1).sensorName("Sensor1").sensorType("Temp").unit("C").device(device).build();
        measurement = Measurement.builder().id(1).value(25.5).timestamp(LocalDateTime.now()).sensor(sensor).build();
        deviceRequest = new DeviceRequest("Device1", "Type1");
    }

    /**
     * Test for the addDevice method
     */
    @Test
    void addDevice_correct() {
        // device is saved
        when(deviceRepository.save(any(Device.class))).thenReturn(device);
        when(devicePresentationMapper.apply(any(Device.class))).thenReturn(new DevicePresentationResponse(1, "Device1", "Type1"));

        DevicePresentationResponse response = deviceService.addDevice(deviceRequest);

        assertThat(response).isNotNull();
        assertThat(response.deviceName()).isEqualTo("Device1");

        verify(deviceRepository).save(any(Device.class));
    }

    /**
     * Test for the addDevice method
     */
    @Test
    void allDevices() {
        // returns list of mapped devices
        List<Device> devices = Collections.singletonList(device);

        when(deviceRepository.findAll()).thenReturn(devices);
        when(devicePresentationMapper.apply(device)).thenReturn(new DevicePresentationResponse(1, "Device1", "Type1"));

        List<DevicePresentationResponse> result = deviceService.getAllDevicesPresentationInfo();

        assertThat(result).hasSize(1);

        verify(deviceRepository).findAll();

        verify(devicePresentationMapper).apply(device);
    }

    /**
     * Test for the getDeviceById method
     */
    @Test
    void returnsvalidDevice() {
        // returns the correct device when exist
        when(deviceRepository.findById(1)).thenReturn(Optional.of(device));

        Device result = deviceService.getDeviceById(1);

        assertThat(result).isEqualTo(device);

        verify(deviceRepository).findById(1);
    }

    /**
     * Test for the deleteDevice method
     */
    @Test
    void valid_deleteDevice() {
        // device is deleted when exists
        when(deviceRepository.findById(1)).thenReturn(Optional.of(device));

        Device result = deviceService.deleteDevice(1);

        assertThat(result).isEqualTo(device);

        verify(deviceRepository).delete(device);
    }


    /**
     * Test for the getDeviceById method when null is returned
     */
    @Test
    void addDevice_requestNUllException() {
        // NullPointerException if device request null
        assertThatThrownBy(() -> deviceService.addDevice(null)).isInstanceOf(NullPointerException.class);
    }


    /**
     * Test for the getDeviceById method when device does not exist
     */
    @Test
    void emptyList_noDevices() {
        // returns an empty list when no devices exist
        when(deviceRepository.findAll()).thenReturn(List.of());

        List<DevicePresentationResponse> result = deviceService.getAllDevicesPresentationInfo();

        assertThat(result).isEmpty();
    }

}
