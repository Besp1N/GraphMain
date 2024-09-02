package com.kacper.backend.anomaly;

import lombok.Builder;

import java.util.List;

@Builder
public record AnomalyResponse(
        List<Integer> measurement_ids
) {
}
