package com.kacper.backend.notification;

import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.measurement.MeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Measurement measurement;

    @BeforeEach
    void setUp() {
        measurement = Measurement.builder().id(1).value(25.5).timestamp(LocalDateTime.now()).build();
    }

    private String createPayload() {
        return """
        {
          "id": 1,
          "message": "Testmessage",
          "type": "info",
          "measurement_id": 1,
          "created_at": "2024-09-01T12:00:00"
        }
        """;
    }


    @Test
    void validPayload() {
        // Notification sent if payload valid
        String validPayload = createPayload();

        when(measurementRepository.findById(anyInt())).thenReturn(Optional.of(measurement));

        notificationService.sendNotification(validPayload);

        verify(messagingTemplate).convertAndSend(eq("/notifications"), any(NotificationResponse.class));
    }

    @Test
    void invalidPayload() {
        // No notif sent if payload invalid
        String invalidPayload = "{ invalid json }";

        notificationService.sendNotification(invalidPayload);

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(NotificationResponse.class));
    }

    @Test
    void measNotFound() {
        // no notif if no measurement
        String payload = createPayload();
        when(measurementRepository.findById(anyInt())).thenReturn(Optional.empty());

        notificationService.sendNotification(payload);

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(NotificationResponse.class));
    }

    @Test
    void emptyList() {
        // empty list if no notif found
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> emptyPage = new PageImpl<>(List.of());

        when(notificationRepository.findAll(pageable)).thenReturn(emptyPage);

        List<NotificationResponse> responses = notificationService.getNotifications(0);

        assertThat(responses).isEmpty();
    }

}

