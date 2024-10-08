//package com.kacper.backend.device;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Tests for the DevicePresentationMapper class
// *
// * @author Kacper Karabinowski
// */
//class DevicePresentationMapperTest {
//
//    /**
//     * Test for the apply method
//     */
//    @Test
//    void shouldReturnDevicePresentationResponse() {
//        // given
//        Device device = new Device(99, "In-99", "Inverter", new ArrayList<>());
//        DevicePresentationMapper devicePresentationMapper = new DevicePresentationMapper();
//
//        // when
//        DevicePresentationResponse devicePresentationResponse = devicePresentationMapper.apply(device);
//
//        // then
//        assertEquals(device.getId(), devicePresentationResponse.id());
//        assertEquals(device.getDeviceName(), devicePresentationResponse.deviceName());
//        assertEquals(device.getDeviceType(), devicePresentationResponse.deviceType());
//    }
//}