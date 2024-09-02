package com.kacper.backend.anomaly;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/anomaly")
public class AnomalyController
{
    private final AnomalyService anomalyService;

    public AnomalyController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping("/{sensorId}")
    public AnomalyResponse getAnomalies(
            @PathVariable String sensorId,
            @RequestParam Integer from,
            @RequestParam Integer to
    ) {
        return anomalyService.getAnomalies(sensorId, from ,to);
    }
}
