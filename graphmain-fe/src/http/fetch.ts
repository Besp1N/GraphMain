import { Device, Measurement, Sensor, } from "../entities"
import { getToken } from "./authUtils";

export const BACKEND_URI = "http://127.0.0.1:8080";
/**
 * Custom error class to signify an error that is directly related to an undesired status code when fetching.
 */
export class HttpError extends Error {
    public statusCode: number;

    constructor(message: string, statusCode: number) {
        super(message);
        this.name = this.constructor.name; // Set the name to the derived class's name
        this.statusCode = statusCode;
    }

    /**
     * Creates an appropriate HttpError subclass based on the HTTP response status.
     * @param res The response object from a fetch request.
     * @returns An instance of a specific HttpError subclass.
     */
    static async from_response(res: Response): Promise<HttpError> {
        const message = await res.text(); // Retrieve the error message from the response body

        switch (res.status) {
            case 400:
                return new BadRequestError(message);
            case 401:
                return new UnauthorizedError(message);
            case 403:
                return new ForbiddenError(message);
            case 404:
                return new NotFoundError(message);
            case 500:
                return new InternalServerError(message);
            default:
                return new HttpError(message || `Unexpected error with status ${res.status}`, res.status);
        }
    }
}
  export class BadRequestError extends HttpError {
    constructor(message = "Bad Request") {
      super(message, 400);
      this.name = "BadRequestError";
    }
  }
  
  export class UnauthorizedError extends HttpError {
    constructor(message = "Unauthorized") {
      super(message, 401);
    }
  }
  
  export class ForbiddenError extends HttpError {
    constructor(message = "Forbidden") {
      super(message, 403);
    }
  }
  
  export class NotFoundError extends HttpError {
    constructor(message = "Not Found") {
      super(message, 404);
    }
  }
  
  export class InternalServerError extends HttpError {
    constructor(message = "Internal Server Error") {
      super(message, 500);
    }
  }

export type MeasurementDataForSensor = {
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
export type Result<T, E> = T | E;
export type Option<T> = T | undefined;

export async function getDevice(id: Device["id"]): Promise<Result<Option<Device>, HttpError>> {
    return await fetchSafe<Device>(`${BACKEND_URI}/api/v1/device/sensor/${id}`, addCredentials({}));
}

/**
 * Function for getting an array of devices with no sensor data.
 * May be extended with optional parameters for filtering.
 */
export async function getDevices(): Promise<Result<Option<Device[]>, HttpError>> {

    return await fetchSafe<Device[]>(`${BACKEND_URI}/api/v1/device/`, addCredentials({}));
}


/**
 * Function for fetching all Measurments in a given span in seconds
 */
export async function getMeasurements(
    sensor: Sensor["id"],
    page: number = 0,
    from?: EpochTimeStamp,
    to?: EpochTimeStamp
): Promise<Result<Option<MeasurementDataForSensor>, HttpError>> {
    if (from === undefined) {
        from = Math.floor((Date.now() / 1000) - (60 * 60 * 24 * 7 * 4 * 10 * 10)); // default to last 7 days
    }
    if (to === undefined) {
        to = Math.floor(Date.now() / 1000); // default to now
    }
    const res = await fetchSafe<MeasurementDataForSensor>(
        `${BACKEND_URI}/api/v1/device/measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`, addCredentials({})
    );
    console.log(res);
    return res;
    // return await fetchSafe<MeasurementDataForSensor>(
    //     `${BACKEND_URI}/api/v1/device/measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`, addCredentials({})
    // );
}
/**
 * Function attaching proper credentials to a request using authUtils.
 * Mutates requestOptions
 * @param requestOptions Request options like in "fetch" 2nd parameter
 */
export function addCredentials(requestOptions: RequestInit) {
  const token = getToken();
  if (requestOptions.headers == undefined) {
    requestOptions.headers = {} as HeadersInit;
  }

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  //@ts-expect-error
 requestOptions.headers["Authorization"] =  `Bearer ${token}`;
 return requestOptions;
  
}
/**
 * Fetch API implementation that never throws and suggests to check if the data is null.
 */
export async function fetchSafe<T>(uri: string, headers?: RequestInit): Promise<Result<Option<T>, HttpError>> {
    try {
        const res = await fetch(uri, headers);
        if (!res.ok) throw HttpError.from_response(res);
      
        const data: T = await res.json();
        return data;
    } catch (err) {
        return err as HttpError;
    }
}
