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
// eslint-disable-next-line @typescript-eslint/no-unused-vars
export async function getMeasurements(sensor: Sensor["id"], page: number = 0, from?: EpochTimeStamp, to?: EpochTimeStamp): Promise<Result<Option<Measurement[]>>> {
    if (from == undefined) {
        from =  Math.floor((Date.now() / 1000) - (60 * 60 * 24 * 7));
    }
    if(to == undefined) {
        to = Math.floor(Date.now() / 1000);
    }
    return await fetchSafe<Measurement[]>(`${BACKEND_URI}/${BackendEndpoint.Device}measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`);

}

export type Result<T> = T | Error;
export type Option<T> = T | undefined;

/**
 * Fetch API implementation that never throws and suggests to check if the data is null.  

 */
export async function fetchSafe<T>(uri: string, headers?: RequestInit): Promise<Result<Option<T>>> {
    let data: Option<T> = undefined;
    try {
        const res = await fetch(uri, headers);
        if (!res.ok) throw new Error(`Invalid response - status ${res.status}.`);
        
        data = await res.json();
    }
    catch (err: unknown) {
        return err as Error;
    }
    return data;
}

