package com.kacper.backend.measurement;

import com.kacper.backend.sensor.Sensor;
import com.kacper.backend.sensor.SensorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Measurement service
 *
 * @author Kacper Karabinowski
 */
@Service
public class MeasurementService
{
    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;


    /**
     * @param measurementRepository is injected automatically by Spring
     * @param sensorService is injected automatically by Spring
     */
    public MeasurementService(
            MeasurementRepository measurementRepository,
            SensorService sensorService
    ) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    /**
     * This function adds a measurement to the database.
     *
     * @param sensorId is the id of the sensor
     * @param measurementRequest is the request body
     * @return Measurement
     */
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

    /**
     * @param measurementId is the id of the measurement
     * @return Measurement
     */
    public Measurement deleteMeasurement(Integer measurementId) {
        Measurement measurement = getMeasurementById(measurementId);
        measurementRepository.delete(measurement);
        return measurement;
    }

    /**
     * @param measurementId is the id of the measurement
     * @return Measurement
     */
    private Measurement getMeasurementById(Integer measurementId) {
        return measurementRepository.findById(measurementId)
                .orElseThrow(() -> new IllegalArgumentException("Measurement with id " + measurementId + " does not exist"));

    }

}
