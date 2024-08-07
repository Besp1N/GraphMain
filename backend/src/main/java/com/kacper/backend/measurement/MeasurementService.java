package com.kacper.backend.measurement;

import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeasurementService
{
    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;


    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public Measurement addMeasurement(Integer sensorId, MeasurementRequest measurementRequest) {
        Sensor sensor = sensorService.getSensorById(sensorId);

        return measurementRepository.save(
                Measurement.builder()
                        .value(measurementRequest.value())
                        .unit(measurementRequest.unit())
                        .timestamp(measurementRequest.timestamp())
                        .sensor(sensor)
                        .build()
        );
    }

    public Measurement deleteMeasurement(Integer measurementId) {
        Measurement measurement = getMeasurementById(measurementId);
        measurementRepository.delete(measurement);
        return measurement;
    }

    private Measurement getMeasurementById(Integer measurementId) {
        return measurementRepository.findById(measurementId)
                .orElseThrow(() -> new IllegalArgumentException("Measurement with id " + measurementId + " does not exist"));

    }

}
