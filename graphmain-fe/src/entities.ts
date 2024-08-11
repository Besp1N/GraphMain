// This file contains all relevant Spring Boot entities mappings that may be serialized and sent via JSON.
export type Entity = Device | Sensor | Measurement;

/**
 * Type representing Device entity in Spring-Boot backend.
 */
export type Device = {
    id: number,
    deviceName: string,
    deviceType: string,
    sensors:  Sensor[] | undefined,
}

/**
 * Type representing Sensor entity in Spring-Boot backend.
 */
export type Sensor = {
    id: number,
    sensorType: string,
    sensorName: string,
    device: Device["id"],
    measurments: Measurement[] | undefined
}
/**
 * Type representing Measurment entity in Spring-Boot backend.
 */
export type Measurement = {
    id: number,
    unit: string,
    value: number,
    timestamp: Date,
    sensor: Sensor["id"] | undefined
}
