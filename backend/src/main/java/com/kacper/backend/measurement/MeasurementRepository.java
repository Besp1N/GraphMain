package com.kacper.backend.measurement;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Measurement repository
 */
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>
{
}
