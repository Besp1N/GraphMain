package com.kacper.backend.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kacper.backend.measurement.Measurement;
import com.kacper.backend.user_notification.UserNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "type",
            nullable = false
    )
    private String type;

    @Column(
            name = "message",
            nullable = false
    )
    private String message;

    @Column(
            name = "created_at",
            nullable = false
    )
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "measurement_id", nullable = false)
    @JsonProperty("measurement_id")
    private Measurement measurement;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserNotification> userNotifications;
}
