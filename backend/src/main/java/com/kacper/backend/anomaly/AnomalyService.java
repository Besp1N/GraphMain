package com.kacper.backend.anomaly;

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

@Service
public class AnomalyService
{
    private final NotificationRepository notificationRepository;

    public AnomalyService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public AnomalyResponse getAnomalies(String sensorId, Integer from, Integer to) {
        LocalDateTime fromDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault());
        LocalDateTime toDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault());

        List<Notification> notifications = notificationRepository.findBySensorIdAndCreatedAtBetween(sensorId, fromDateTime, toDateTime);

        Set<Integer> uniqueMeasurementIds = new HashSet<>();
        List<Integer> measurementIds = notifications.stream()
                .map(notification -> notification.getMeasurement().getId())
                .filter(uniqueMeasurementIds::add)
                .collect(Collectors.toList());

        return AnomalyResponse.builder()
                .measurement_ids(measurementIds)
                .build();
    }
}
