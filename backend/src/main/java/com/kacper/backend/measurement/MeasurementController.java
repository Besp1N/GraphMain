package com.kacper.backend.measurement;

import org.springframework.web.bind.annotation.*;

/**
 * Measurement controller class.
 *
 * @author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/v1/measurement")
public class MeasurementController
{
    private final MeasurementService measurementService;

    /**
     * @param measurementService is injected automatically by Spring
     */
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    /**
     * Adds a measurement to the database.
     *
     * @param sensorId is the id of the sensor
     * @param measurementRequest is the request body
     * @return Measurement
     */
    @PostMapping("/{sensorId}")
    public Measurement addMeasurement(
            @PathVariable Integer sensorId,
            @RequestBody MeasurementRequest measurementRequest
    ) {
        return measurementService.addMeasurement(sensorId, measurementRequest);
    }

    /**
     * Deletes a measurement from the database.
     *
     * @param measurementId is the id of the measurement
     * @return Measurement
     */
    @DeleteMapping("/{measurementId}")
    public Measurement deleteMeasurement(@PathVariable Integer measurementId) {
        return measurementService.deleteMeasurement(measurementId);
    }
}
