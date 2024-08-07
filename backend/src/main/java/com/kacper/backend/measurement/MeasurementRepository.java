package com.kacper.backend.measurement;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Measurement repository
 */
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>
{
    Page<Measurement> findAllBySensorId(Integer sensorId, org.springframework.data.domain.Pageable pageable);
}
