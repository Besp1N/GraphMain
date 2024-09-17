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
     * @param deviceId is the id of the device
     * @param from is the start date
     * @param to is the end date
     * @return list of notifications by sensor id and created at between
     */
    @Query("SELECT n " +
            "FROM Notification n " +
            "WHERE n.device.id = :deviceId " +
            "AND n.createdAt BETWEEN :from AND :to")
    List<Notification> findBySensorIdAndCreatedAtBetween(
            @Param("deviceId") Integer deviceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
