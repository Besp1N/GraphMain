import { Device, } from "../entities"
export enum BackendEndpoint  {
    Device = "api/v1/device/",
    Sensor = "api/v1/sensor/"    
}
export const BACKEND_URI = "http://127.0.0.1:8080";
/**
 * Function for fetching a single device entity via id. Includes sensor data.
 */
export async function getDevice(id: Device["id"]): Promise<Option<Device>> {
    return await fetchEntity<Device>(`${BACKEND_URI}/${BackendEndpoint.Device}sensor/${id}`);
}  

/**
 * Function for getting an array of devices with no sensor data.
 * May be extended with optional parameters for filtering.
 */
export async function getDevices(): Promise<Option<Device[]>> {
    return await fetchEntity<Device[]>(`${BACKEND_URI}/${BackendEndpoint.Device}`);
}
export type Option<T> = T | undefined;


export async function fetchEntity<T>(uri: string, headers?: RequestInit): Promise<Option<T>> {
    let data;
    try {
        const res = await fetch(uri, headers);
        if (!res.ok) throw new Error(`Invalid response - status ${res.status}.`);
        data = await res.json();
    }
    catch (err: unknown) {
        console.error(err);
        return undefined;
    }
    return data;
    
}
