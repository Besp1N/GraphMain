package com.kacper.backend.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer>
{
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
