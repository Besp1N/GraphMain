package com.kacper.backend.anomaly;

import lombok.Builder;

import java.util.List;

/**
 * @param measurement_ids ids of measurements that are anomalies
 *
 * @Author Kacper Karabinowski 
 */
@Builder
public record AnomalyResponse(
        List<Integer> measurement_ids
) {
}
