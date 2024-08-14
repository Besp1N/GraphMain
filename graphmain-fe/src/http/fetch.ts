import { Device, Measurement, Sensor, } from "../entities"

export const BACKEND_URI = "http://127.0.0.1:8080";

/**
 * Function for fetching a single device entity via id. Includes sensor data.
 */
export async function getDevice(id: Device["id"]): Promise<Result<Option<Device>>> {
 return await fetchSafe<Device>(`${BACKEND_URI}/api/v1/device/sensor/${id}`);
}

/**
 * Function for getting an array of devices with no sensor data.
 * May be extended with optional parameters for filtering.
 */
export async function getDevices(): Promise<Result<Option<Device[]>>> {
    return await fetchSafe<Device[]>(`${BACKEND_URI}/api/v1/device/`);

}

export type MeasurmentDataForSensor = {
    
        deviceId: number,
        deviceName: string,
        deviceType: string,
        totalPages: number,
        sensor: {
            id: number,
            sensorName: string,
            sensorType: string,
            unit: string,
            measurementList: Measurement[]
        }
    
};
/**
 * Fetch API implementation that never throws and suggests to check if the data is null.

 */
export async function getMeasurements(
    sensor: Sensor["id"],
    page: number = 0,
    from?: EpochTimeStamp,
    to?: EpochTimeStamp
): Promise<Result<Option<MeasurmentDataForSensor>>> {

    if (from === undefined) {
        from = Math.floor((Date.now() / 1000) - (60 * 60 * 24 * 7)); // default to last 7 days
    }
    if (to === undefined) {
        to = Math.floor(Date.now() / 1000); // default to now
    }
    return await fetchSafe<MeasurmentDataForSensor>(
        `${BACKEND_URI}/api/v1/device/measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`
    );
   
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

