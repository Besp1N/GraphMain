package com.kacper.backend.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kacper.backend.sensor.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "measurements")
public class Measurement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "value",
            nullable = false
    )
    private Double value;

    @Column(
            name = "unit",
            nullable = false
    )
    private String unit;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;


}
