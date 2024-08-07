package com.kacper.backend.measurement;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController
{
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/{sensorId}")
    public Measurement addMeasurement(
            @PathVariable Integer sensorId,
            @RequestBody MeasurementRequest measurementRequest
    ) {
        return measurementService.addMeasurement(sensorId, measurementRequest);
    }

    @DeleteMapping("/{measurementId}")
    public Measurement deleteMeasurement(@PathVariable Integer measurementId) {
        return measurementService.deleteMeasurement(measurementId);
    }
}
