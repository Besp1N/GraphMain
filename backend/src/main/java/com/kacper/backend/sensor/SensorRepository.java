package com.kacper.backend.sensor;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Sensor repository
 *
 * @author Kacper Karabinowski
 */
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
