package com.kacper.backend.measurement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Measurement repository
 */
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>
{
    Page<Measurement> findAllBySensorIdAndTimestampBetween(Integer sensorId, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable);

    @Query(value = "SELECT * FROM measurements " +
            "WHERE sensor_id = :sensorId " +
            "AND MOD(id, 10) = 0",
            nativeQuery = true)
    List<Measurement> findEveryTenthMeasurementBySensorId(@Param("sensorId") Integer sensorId);

    @Query(value = "SELECT * FROM (" +
            "SELECT m.*, ROW_NUMBER() OVER (ORDER BY m.id) AS row_num " +
            "FROM measurements m " +
            "WHERE m.sensor_id = :sensorId AND m.timestamp BETWEEN :fromTime AND :toTime) subquery " +
            "WHERE MOD(subquery.row_num, :interval) = 0",
            nativeQuery = true
    )
    List<Measurement> findMeasurementsBySensorIdWithIntervalAndTimestampBetween(@Param("sensorId") Integer sensorId, @Param("interval") Integer interval, @Param("fromTime") LocalDateTime fromTime, @Param("toTime") LocalDateTime toTime);

    long countBySensorIdAndTimestampBetween(Integer sensorId, LocalDateTime fromTime, LocalDateTime toTime);
}
