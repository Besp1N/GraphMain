package com.kacper.backend.sensor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kacper.backend.device.Device;
import com.kacper.backend.measurement.Measurement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the sensor entity in the database
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensors")
public class Sensor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "sensor_type",
            nullable = false
    )
    private String sensorType;

    @Column(
            name = "sensor_name",
            nullable = false
    )
    private String sensorName;

    @Column(
            name = "unit",
            nullable = false
    )
    private String unit;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Measurement> measurements;

}
