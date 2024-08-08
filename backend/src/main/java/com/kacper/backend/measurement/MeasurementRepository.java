package com.kacper.backend.measurement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * Measurement repository
 */
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>
{
    Page<Measurement> findAllBySensorIdAndTimestampBetween(Integer sensorId, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable);
}
