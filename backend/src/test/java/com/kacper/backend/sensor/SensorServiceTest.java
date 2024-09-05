package com.kacper.backend.sensor;

import com.kacper.backend.device.Device;
import com.kacper.backend.device.DeviceService;
import com.kacper.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    private static final Integer DEVICE_ID = 1;
    private static final Integer SENSOR_ID = 10;
    private static final String SENSOR_NAME = "Temp Test";
    private static final String SENSOR_TYPE = "Temp";
    private static final String UNIT = "C";

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private SensorService sensorService;

    private SensorRequest sensorRequest;
    private Device device;

    @BeforeEach
    void setUp() {
        sensorRequest = new SensorRequest(SENSOR_NAME, SENSOR_TYPE, UNIT);
        device = Device.builder().id(DEVICE_ID).deviceName("Device 1").build();
    }

    @Test
    void addSensor() {
        // sensor added to device successfully yipeeeeee
        when(deviceService.getDeviceById(DEVICE_ID)).thenReturn(device);

        Device result = sensorService.addSensorToDevice(DEVICE_ID, sensorRequest);

        verify(sensorRepository).save(any(Sensor.class));

        assertThat(result).isEqualTo(device);
    }



    @Test
    void addSensor_deviceNotFound() {
        // exception wif device not found
        when(deviceService.getDeviceById(DEVICE_ID)).thenThrow(new ResourceNotFoundException("Device Not found"));

        assertThatThrownBy(() -> sensorService.addSensorToDevice(DEVICE_ID, sensorRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Device Not found");

        verify(sensorRepository, never()).save(any(Sensor.class));
    }

    @Test
    void getSensor() {
        // sensor get success yay
        Sensor sensor = Sensor.builder().id(SENSOR_ID).sensorName(SENSOR_NAME).build();

        when(sensorRepository.findById(SENSOR_ID)).thenReturn(java.util.Optional.of(sensor));

        Sensor result = sensorService.getSensorById(SENSOR_ID);

        assertThat(result).isEqualTo(sensor);
    }



    @Test
    void getSensor_notFound() {
        // exception if sensor not found
        when(sensorRepository.findById(SENSOR_ID)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> sensorService.getSensorById(SENSOR_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Sensor " + SENSOR_ID + " Not found");
    }

    @Test
    void deleteSensor() {
        // makes sure sensor deleted works
        Sensor sensor = Sensor.builder().id(SENSOR_ID).sensorName(SENSOR_NAME).build();

        when(sensorRepository.findById(SENSOR_ID)).thenReturn(java.util.Optional.of(sensor));

        Sensor deletedSensor = sensorService.deleteSensorById(SENSOR_ID);

        verify(sensorRepository).delete(sensor);

        assertThat(deletedSensor).isEqualTo(sensor);
    }

    @Test
    void addSensor_correct() {
        // sensor saved with correct properties

        when(deviceService.getDeviceById(DEVICE_ID)).thenReturn(device);

        sensorService.addSensorToDevice(DEVICE_ID, sensorRequest);

        verify(sensorRepository).save(argThat(sensor ->
                sensor.getSensorName().equals(SENSOR_NAME) &&
                        sensor.getSensorType().equals(SENSOR_TYPE) &&
                        sensor.getUnit().equals(UNIT) &&
                        sensor.getDevice().equals(device)
        ));
    }


    @Test
    void deleteSensor_alreadyDeleted() {
        // exception if trying to delete deleted sensor
        when(sensorRepository.findById(SENSOR_ID)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> sensorService.deleteSensorById(SENSOR_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Sensor " + SENSOR_ID + " Not found");


        verify(sensorRepository, never()).delete(any(Sensor.class));
    }


}
