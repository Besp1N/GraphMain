import { Entity, Device, } from "../entities"
export enum BackendEndpoint  {
    Devices = "api/device/",
    Sensors = "api/sensor/"    
}
const backendURL = "http://127.0.0.1:8080";
/**
 * Function for fetching a single device entity via id.
 */
export async function getDevice(id: Device["id"]): Promise<Option<Device>> {
    return await fetchEntity<Device>(`${backendURL}/${BackendEndpoint.Devices}${id}`);
}  
export type Option<T> = T | undefined;


/**
 * Function for getting an array of devices.
 * May be extended with optional parameters for filtering.
 */
export async function fetchEntity<T extends Entity>(uri: string, headers?: RequestInit): Promise<Option<T>> {
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
