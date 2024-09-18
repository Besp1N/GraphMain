package com.kacper.backend.anomaly;

import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for handling anomaly requests
 *
 * @Author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/v1/anomaly")
public class AnomalyController
{
    private final AnomalyService anomalyService;

    /**
     * @param anomalyService service for handling anomaly requests
     */
    public AnomalyController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    /**
     * @param deviceId is a device id
     * @param from start of the time range
     * @param to end of the time range
     * @return AnomalyResponse with ids of anomalies
     */
    @GetMapping("/{deviceId}")
    public List<AnomalyResponse> getAnomalies(
            @PathVariable Integer deviceId,
            @RequestParam Integer from,
            @RequestParam Integer to
    ) {
        return anomalyService.getAnomalies(deviceId, from ,to);
    }
}
