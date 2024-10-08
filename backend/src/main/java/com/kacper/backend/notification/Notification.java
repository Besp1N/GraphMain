package com.kacper.backend.notification;

import com.kacper.backend.device.Device;
import com.kacper.backend.measurement.Measurement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the notification entity in the database
 *
 * @Author Kacper Karabinowski
 */
@Entity
@Table(name = "notifications")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "message",
            nullable = false
    )
    private String message;

    @Column(
            name = "type",
            nullable = false
    )
    private String type;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    // change to device id
//    @ManyToOne
//    @JoinColumn(name = "measurement_id", nullable = false)
//    private Measurement measurement;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}
