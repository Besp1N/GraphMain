import { ROLE } from "./http/authUtils";

// This file contains all relevant Spring Boot entities mappings that may be serialized and sent via JSON.
export type Entity = Device | Sensor | Measurement | Notification;

/**
 * Type representing Device entity in Spring-Boot backend.
 */
export type Device = {
  id: number;
  deviceName: string;
  deviceType: string;
  sensors: Sensor[] | undefined;
};

/**
 * Type representing Sensor entity in Spring-Boot backend.
 */
export type Sensor = {
  id: number;
  sensorType: string;
  sensorName: string;
  unit: string;
  device: Device["id"];
  measurementList: Measurement[] | undefined;
};
/**
 * Type representing Measurment entity in Spring-Boot backend.
 */
export type Measurement = {
  id: number;
  value: number;
  timestamp: string;
  sensor: Sensor["id"] | undefined;
};

export type NotificationEntityType = "warning" | "info" | "error";
export type NotificationEntity = {
  id: number;
  message: string;
  type: NotificationEntityType;
  created_at: string;
  device_id: Device["id"];
};

export type User = {
  id: number;
  email: string;
  name: string;
  lastName: string;
  role: ROLE;
  createdAt: string;
};
