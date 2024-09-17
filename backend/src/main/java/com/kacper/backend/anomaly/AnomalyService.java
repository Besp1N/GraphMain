package com.kacper.backend.anomaly;

import com.kacper.backend.device.Device;
import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.notification.Notification;
import com.kacper.backend.notification.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling anomaly requests
 *
 * @Author Kacper Karabinowski
 */
@Service
public class AnomalyService
{
    private final NotificationRepository notificationRepository;

    /**
     * @param notificationRepository repository for handling notification requests
     */
    public AnomalyService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * @param deviceId is a device id
     * @param from start of the time range
     * @param to end of the time range
     * @return AnomalyResponse -  ids array of anomalies
     */
    public AnomalyResponse getAnomalies(
            Integer deviceId,
            Integer from,
            Integer to
    ) {
        LocalDateTime fromDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault());
        LocalDateTime toDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault());

        List<Notification> notifications = notificationRepository.findBySensorIdAndCreatedAtBetween(
                deviceId,
                fromDateTime,
                toDateTime
        );

        Set<Integer> uniqueMeasurementIds = new HashSet<>();
        List<Device> devices = notifications.stream()
                .map(Notification::getDevice)
                .filter(device -> uniqueMeasurementIds.add(device.getId()))
                .collect(Collectors.toList());

        return AnomalyResponse.builder()
                .devices(devices)
                .build();
    }
}
