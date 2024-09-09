package com.kacper.backend.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.measurement.MeasurementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Notification service
 *
 * @Author Kacper Karabinowski
 */
@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MeasurementRepository measurementRepository;
    private final NotificationRepository notificationRepository;

    /**
     * @param messagingTemplate is template for sending messages
     * @param measurementRepository is repository for measurements
     * @param notificationRepository is repository for notifications
     */
    public NotificationService(
            SimpMessagingTemplate messagingTemplate,
            MeasurementRepository measurementRepository,
            NotificationRepository notificationRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.measurementRepository = measurementRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Sends notification on mail
     *
     * @param payload is the payload of the notification
     */
    public void sendNotification(String payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            NotificationMapper notificationMapper = objectMapper.readValue(payload, NotificationMapper.class);
            Measurement measurement = measurementRepository.findById(
                    notificationMapper.measurement_id()
            ).orElseThrow();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime createdAt = LocalDateTime.parse(notificationMapper.created_at(), formatter);

            NotificationResponse notification = NotificationResponse.builder()
                    .id(notificationMapper.id())
                    .message(notificationMapper.message())
                    .type(notificationMapper.type())
                    .created_at(createdAt)
                    .measurement(measurement)
                    .build();

            messagingTemplate.convertAndSend("/notifications", notification);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @param numPage is the number of the page
     * @return List of notifications
     */
    public List<NotificationResponse> getNotifications(Integer numPage) {
        int pageSize = 2;

        PageRequest pageable = PageRequest.of(numPage, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notificationsPage = notificationRepository.findAll(pageable);
        List<Notification> notifications = notificationsPage.getContent();
        int totalPages = notificationsPage.getTotalPages();

        return notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .created_at(notification.getCreatedAt())
                        .id(notification.getId())
                        .type(notification.getType())
                        .message(notification.getMessage())
                        .device_id(notification.getMeasurement().getSensor().getDevice().getId())
                        .sensor_id(notification.getMeasurement().getSensor().getId())
                        .measurement(notification.getMeasurement())
                        .totalPages(totalPages)
                        .build()
                ).collect(Collectors.toList());
    }
}