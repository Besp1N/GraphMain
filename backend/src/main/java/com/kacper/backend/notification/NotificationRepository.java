package com.kacper.backend.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Notification repository
 *
 * @Author Kacper Karabinowski
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer>
{
    /**
     * @param sensorId is the id of the sensor
     * @param from is the start date
     * @param to is the end date
     * @return list of notifications by sensor id and created at between
     */
    @Query("SELECT n " +
            "FROM Notification n " +
            "WHERE n.measurement.sensor.id = :sensorId " +
            "AND n.createdAt BETWEEN :from AND :to")
    List<Notification> findBySensorIdAndCreatedAtBetween(
            @Param("sensorId") String sensorId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
