package com.kacper.backend.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.measurement.MeasurementRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MeasurementRepository measurementRepository;

    public NotificationService(
            SimpMessagingTemplate messagingTemplate,
            MeasurementRepository measurementRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.measurementRepository = measurementRepository;
    }

    public void sendNotification(String payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            NotificationMapper notificationMapper = objectMapper.readValue(payload, NotificationMapper.class);
            Measurement measurement = measurementRepository.findById(notificationMapper.measurement_id()).orElseThrow();

            NotificationResponse notification = NotificationResponse.builder()
                    .id(notificationMapper.id())
                    .message(notificationMapper.message())
                    .type(notificationMapper.type())
                    .createdAt(notificationMapper.created_at())
                    .measurement(measurement)
                    .build();

            messagingTemplate.convertAndSend("/notifications", notification);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}