package com.kacper.backend.device;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Device repository
 *
 * @author Kacper Karabinowski
 */
public interface DeviceRepository extends JpaRepository<Device, Integer>
{
}
