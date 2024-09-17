package com.kacper.backend.anomaly;

import com.kacper.backend.device.Device;
import com.kacper.backend.measurement.Measurement;
import lombok.Builder;

import java.util.List;

/**
 * Anomaly response class.
 *
 * @Author Kacper Karabinowski 
 */
@Builder
public record AnomalyResponse(
        List<Device> devices
) {
}
