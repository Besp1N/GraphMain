package com.kacper.backend.measurement;

import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private MeasurementService measurementService;

    private Sensor sensor;
    private Measurement measurement;
    private MeasurementRequest measurementRequest;

    @BeforeEach
    void setUp() {
        sensor = Sensor.builder()
                .id(1)
                .sensorType("Temperature")
                .sensorName("TempSens")
                .unit("C")
                .build();

        measurement = Measurement.builder()
                .id(1)
                .value(26.2)
                .timestamp(LocalDateTime.now())
                .sensor(sensor)
                .build();

        measurementRequest = new MeasurementRequest(26.2, LocalDateTime.now());

    }



    @Test
    void saveMeasurement() {
        // adding measurement, checks sensor exist, output=save meas
        when(sensorService.getSensorById(1)).thenReturn(sensor);
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);
        Measurement savedMeasurement = measurementService.addMeasurement(1, measurementRequest);

        assertThat(savedMeasurement).isNotNull();
        assertThat(savedMeasurement.getValue()).isEqualTo(26.2);
        assertThat(savedMeasurement.getSensor()).isEqualTo(sensor);

        verify(sensorService).getSensorById(1);
        verify(measurementRepository).save(any(Measurement.class));
    }

    @Test
    void addMeasurement_throwException() {
        // same as previous, but sensor=false, output=exception
        when(sensorService.getSensorById(1)).thenThrow(new IllegalArgumentException("Sensor with id 1 not found"));


        assertThatThrownBy(() -> measurementService.addMeasurement(1, measurementRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sensor with id 1 not found");

        verify(sensorService).getSensorById(1);

        verify(measurementRepository, never()).save(any(Measurement.class));
    }

    @Test
    void deleteMeasurement() {
        // deleting valid measurements
        when(measurementRepository.findById(1)).thenReturn(Optional.of(measurement));

        Measurement deletedMeasurement = measurementService.deleteMeasurement(1);

        assertThat(deletedMeasurement).isEqualTo(measurement);

        verify(measurementRepository).delete(measurement);


    }



    @Test
    void deleteMeasurement_throwException() {
        // same as previous, meaurement exist = false, output-exception
        when(measurementRepository.findById(1)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> measurementService.deleteMeasurement(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Measurement with id 1 does not exist");

        verify(measurementRepository, never()).delete(any(Measurement.class));
    }


    @Test
    void getMeasurement() {

        when(measurementRepository.findById(1)).thenReturn(Optional.of(measurement));
        Measurement foundMeasurement = measurementService.deleteMeasurement(1);

        assertThat(foundMeasurement).isNotNull();
        assertThat(foundMeasurement.getId()).isEqualTo(1);
        assertThat(foundMeasurement.getValue()).isEqualTo(26.2);

    }


    @Test
    void getMeasurementById_whenMeasurementDoesNotExist_shouldThrowException() {
        // same as previous, but meas=non exit, output - exception
        when(measurementRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> measurementService.deleteMeasurement(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Measurement with id 1 does not exist");
    }
}
