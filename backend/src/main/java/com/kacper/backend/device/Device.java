package com.kacper.backend.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kacper.backend.notification.Notification;
import com.kacper.backend.sensor.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the device entity in the database
 *
 * @Author Kacper Karabinowski
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "device_name",
            nullable = false
    )
    private String deviceName;

    @Column(
            name = "device_type",
            nullable = false
    )
    private String deviceType;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors;

    // add list of notifications
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications;




}

