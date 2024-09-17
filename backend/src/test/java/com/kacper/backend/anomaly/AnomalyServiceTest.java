//package com.kacper.backend.anomaly;
//
//import com.kacper.backend.notification.Notification;
//import com.kacper.backend.notification.NotificationRepository;
//import com.kacper.backend.measurement.Measurement;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//
///**
// * Tests for AnomalyService
// *
// * @Author Sabina Kubiyeva
// */
//@ExtendWith(MockitoExtension.class)
//class AnomalyServiceTest {
//
//    @Mock
//    private NotificationRepository notificationRepository;
//
//    @InjectMocks
//    private AnomalyService anomalyService;
//
//    private Notification notification1;
//    private Notification notification2;
//
//    /**
//     * Set up before each test
//     */
//    @BeforeEach
//    void setUp() {
//        Measurement measurement1 = Measurement.builder().id(1).build();
//        Measurement measurement2 = Measurement.builder().id(2).build();
//
//        notification1 = Notification.builder()
//                .id(1)
//                .message("Anomaly detected")
//                .createdAt(LocalDateTime.now())
//                .measurement(measurement1)
//                .build();
//
//        notification2 = Notification.builder()
//                .id(2)
//                .message("Another anomaly detected")
//                .createdAt(LocalDateTime.now())
//                .measurement(measurement2)
//                .build();
//    }
//
//    /**
//     * Test for getAnomalies method
//     */
//    @Test
//    void notifExists_anomaly() {
//        // returns AnomalyResponse when notif found
//
//        when(notificationRepository.findBySensorIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(Arrays.asList(notification1, notification2));
//
//        AnomalyResponse response = anomalyService.getAnomalies("sensor1", 0, 100);
//
//        assertThat(response).isNotNull();
//        assertThat(response.measurements()).extracting(Measurement::getId).containsExactlyInAnyOrder(1, 2);
//        verify(notificationRepository).findBySensorIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
//    }
//
//    /**
//     * Test for getAnomalies method when no notif found
//     */
//    @Test
//    void noNotif_emptyResponse() {
//        // empty AnomalyResponse when no notif found
//        when(notificationRepository.findBySensorIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(List.of());
//
//        AnomalyResponse response = anomalyService.getAnomalies("sensor1", 0, 100);
//
//        assertThat(response).isNotNull();
//        assertThat(response.measurements()).isEmpty();
//        verify(notificationRepository).findBySensorIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
//    }
//
//    /**
//     * Test for getAnomalies method, filters out duplicate measurement ids
//     */
//    @Test
//    void uniqueMeasurement() {
//        // filters out duplicate measurement ids from notifs
//
//        notification2.setMeasurement(notification1.getMeasurement());
//        when(notificationRepository.findBySensorIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(Arrays.asList(notification1, notification2));
//
//        AnomalyResponse response = anomalyService.getAnomalies("sensor1", 0, 100);
//
//        assertThat(response).isNotNull();
//        assertThat(response.measurements()).extracting(Measurement::getId).containsExactly(1);
//    }
//}