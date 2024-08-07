// This file contains all relevant Spring Boot entities mappings that may be serialized and sent via JSON.
export type Entity = Device | Sensor | Measurment;

/**
 * Type representing Device entity in Spring-Boot backend.
 */
export type Device = {
    id: number,
    deviceName: string,
    deviceType: string,
    sensors:  Sensor["id"][] | undefined,
}

/**
 * Type representing Sensor entity in Spring-Boot backend.
 */
export type Sensor = {
    id: number,
    sensorType: string,
    sensorName: string,
    device: Device["id"],
    measurments: Measurment["id"][] | undefined
}
/**
 * Type representing Measurment entity in Spring-Boot backend.
 */
export type Measurment = {
    id: number,
    unit: string,
    timestap: Date,
    sensor: Sensor["id"] | undefined
}


