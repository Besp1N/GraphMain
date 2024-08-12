//package com.kacper.backend.device;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.kacper.backend.measurement.Measurement;
//import com.kacper.backend.measurement.MeasurementRepository;
//import com.kacper.backend.sensor.Sensor;
//import com.kacper.backend.sensor.SensorRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Tests for the DeviceService class
// *
// * @author Kacper Karabinowski
// */
//@ExtendWith(MockitoExtension.class)
//public class DeviceServiceTest {
//
//    @Mock
//    private DeviceRepository deviceRepository;
//
//    @Mock
//    private DevicePresentationMapper devicePresentationMapper;
//
//    @Mock
//    private SensorRepository sensorRepository;
//
//    @Mock
//    private MeasurementRepository measurementRepository;
//
//    @InjectMocks
//    private DeviceService deviceService;
//
//    private Sensor sensor;
//    private Device device;
//    private List<Measurement> measurements;
//
//    @BeforeEach
//    void setUp() {
//        device = new Device(1, "Device1", "Type1", Collections.emptyList());
//        sensor = new Sensor(1, "Sensor1", "Type1", device, Collections.emptyList());
//        measurements = Collections.singletonList(new Measurement(1, 20.0, "C", LocalDateTime.now(), sensor));
//    }
//
//    @Test
//    void testGetDeviceSensorMeasurementPresentationInfo() {
//        // Given
//        Integer sensorId = 1;
//        int numPage = 0;
//        int from = 1722992400;
//        int to = 1723078800;
//
//        LocalDateTime fromTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault());
//        LocalDateTime toTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault());
//
//
//        PageRequest pageable = PageRequest.of(numPage, 5);
//        Page<Measurement> measurementsPage = new PageImpl<>(measurements);
//
//        when(sensorRepository.findById(sensorId)).thenReturn(java.util.Optional.of(sensor));
//        when(measurementRepository.findAllBySensorIdAndTimestampBetween(sensorId, fromTime, toTime, pageable)).thenReturn(measurementsPage);
//
//        // When
//        DeviceMeasurementPresentation result = deviceService.getDeviceSensorMeasurementPresentationInfo(sensorId, numPage, from, to);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(device.getId(), result.deviceId());
//        assertEquals(device.getDeviceName(), result.deviceName());
//        assertEquals(device.getDeviceType(), result.deviceType());
//        assertEquals(sensor.getId(), result.sensor().id());
//        assertEquals(sensor.getSensorName(), result.sensor().sensorName());
//        assertEquals(sensor.getSensorType(), result.sensor().sensorType());
//        assertEquals(measurements, result.sensor().measurementList());
//
//        verify(sensorRepository, times(1)).findById(sensorId);
//        verify(measurementRepository, times(1)).findAllBySensorIdAndTimestampBetween(sensorId, fromTime, toTime, pageable);
//    }
//
//}