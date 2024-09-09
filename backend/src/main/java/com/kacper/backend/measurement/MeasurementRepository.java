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
 *
 * @Author Kacper Karabinowski
 */
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>
{
    /**
     * Returns all measurements by sensor id with interval
     *
     * @param sensorId is the id of the sensor
     * @param fromTime is the start time
     * @param toTime is the end time
     * @param pageable is the page
     * @return page of measurements
     */
    Page<Measurement> findAllBySensorIdAndTimestampBetween(Integer sensorId, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable);

    /**
     * Returns all measurements by sensor id with interval
     *
     * @param sensorId is the id of the sensor
     * @return list of measurements
     */
    @Query(value = "SELECT * FROM measurements " +
            "WHERE sensor_id = :sensorId " +
            "AND MOD(id, 10) = 0",
            nativeQuery = true)
    List<Measurement> findEveryTenthMeasurementBySensorId(@Param("sensorId") Integer sensorId);

    /**
     * Returns all measurements by sensor id with interval
     *
     * @param sensorId is the id of the sensor
     * @param interval is the interval
     * @param fromTime is the start time
     * @param toTime is the end time
     * @return list of measurements
     */
    @Query(value = "SELECT * FROM (" +
            "SELECT m.*, ROW_NUMBER() OVER (ORDER BY m.id) AS row_num " +
            "FROM measurements m " +
            "WHERE m.sensor_id = :sensorId AND m.timestamp BETWEEN :fromTime AND :toTime) subquery " +
            "WHERE MOD(subquery.row_num, :interval) = 0",
            nativeQuery = true
    )
    List<Measurement> findMeasurementsBySensorIdWithIntervalAndTimestampBetween(@Param("sensorId") Integer sensorId, @Param("interval") Integer interval, @Param("fromTime") LocalDateTime fromTime, @Param("toTime") LocalDateTime toTime);

    /**
     * Returns count of measurements by sensor id with interval
     *
     * @param sensorId is the id of the sensor
     * @param fromTime is the start time
     * @param toTime is the end time
     * @return count of measurements
     */
    long countBySensorIdAndTimestampBetween(Integer sensorId, LocalDateTime fromTime, LocalDateTime toTime);
}
