import { Device, Measurement, Sensor, } from "../entities"
export enum BackendEndpoint  {
    Device = "api/v1/device/",
    Sensor = "api/v1/sensor/"
}
export const BACKEND_URI = "http://127.0.0.1:8080";

/**
 * Function for fetching a single device entity via id. Includes sensor data.
 */
export async function getDevice(id: Device["id"]): Promise<Result<Option<Device>>> {
 return await fetchSafe<Device>(`${BACKEND_URI}/${BackendEndpoint.Device}sensor/${id}`);
}

/**
 * Function for getting an array of devices with no sensor data.
 * May be extended with optional parameters for filtering.
 */
export async function getDevices(): Promise<Result<Option<Device[]>>> {
    return await fetchSafe<Device[]>(`${BACKEND_URI}/${BackendEndpoint.Device}`);

}

/**
 * Fetch API implementation that never throws and suggests to check if the data is null.

 */
export async function getMeasurements(
    sensor: Sensor["id"],
    page: number = 0,
    from?: EpochTimeStamp,
    to?: EpochTimeStamp
): Promise<Result<Option<{ measurements: Measurement[], totalPages: number }>>> {
    if (from === undefined) {
        from = Math.floor((Date.now() / 1000) - (60 * 60 * 24 * 7)); // default to last 7 days
    }
    if (to === undefined) {
        to = Math.floor(Date.now() / 1000); // default to now
    }

    try {
        const response = await fetchSafe<{
            deviceId: number,
            deviceName: string,
            deviceType: string,
            totalPages: number,
            sensor: {
                id: number,
                sensorName: string,
                sensorType: string,
                measurementList: Measurement[]
            }
        }>(
            `${BACKEND_URI}/${BackendEndpoint.Device}measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`
        );

        // Check if response is an Error
        if (response instanceof Error) {
            throw response;
        }

        // Ensure the response contains the necessary data
        if (response && response.sensor) {
            return {
                measurements: response.sensor.measurementList, // Path to measurementList
                totalPages: response.totalPages
            };
        } else {
            throw new Error('Invalid data format from backend');
        }
    } catch (error) {
        return error as Error;
    }
}



export type Result<T> = T | Error;
export type Option<T> = T | undefined;

/**
 * Fetch API implementation that never throws and suggests to check if the data is null.

 */
export async function fetchSafe<T>(uri: string, headers?: RequestInit): Promise<Result<Option<T>>> {
    try {
        const res = await fetch(uri, headers);
        if (!res.ok) {
            throw new Error(`Invalid response - status ${res.status}.`);
        }
        const data: T = await res.json();
        return data;
    } catch (err) {
        return err as Error;
    }
}

