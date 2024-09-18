import {
  Device,
  NotificationEntity,
  NotificationEntityType,
  Sensor,
  User,
} from "../entities";
import { getToken, ROLE } from "./authUtils";

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
   * Creates an appropriate HttpError subclass based on the HTTP response status providing a generic message.
   * @param res The response object from a fetch request.
   * @returns An instance of a specific HttpError subclass.
   */
  static async from_response(res: Response): Promise<HttpError> {
    const message = await res.text(); // Retrieve the error message from the response body

    let text = message;
    if (!text) {
      switch (res.status) {
        case 400:
          text = "A bad request has been sent to the server.";
          break;
        case 401:
          text = "You have to be logged in to access this resource.";
          break;
        case 403:
          text = "You don't have permission to this resource.";
          break;
        case 404:
          text = "The resource you're looking for doesn't exist.";
          break;
        case 500:
          text =
            "Internal server error. Please try again later or concat IT admin.";
          break;
        default:
          text = "Unexpected server error";
          break;
      }
    }
    return new HttpError(text, res.status);
  }
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
  requestOptions.headers["Authorization"] = `Bearer ${token}`;
  return requestOptions;
}

export type Result<T, E> = T | E;
export type Option<T> = T | undefined;

export async function getDevice(
  id: Device["id"]
): Promise<Result<Option<Device>, HttpError>> {
  return await fetchSafe<Device>(
    `${BACKEND_URI}/api/v1/device/sensor/${id}`,
    addCredentials({})
  );
}

/**
 * Function for getting an array of devices with no sensor data.
 * May be extended with optional parameters for filtering.
 */
export async function getDevices(): Promise<
  Result<Option<Device[]>, HttpError>
> {
  return await fetchSafe<Device[]>(
    `${BACKEND_URI}/api/v1/device/`,
    addCredentials({})
  );
}

/**
 * Return type of GET for Measurements HTTP request to backend.
 * Returned from getMeasurements().
 */
export type MeasurementDataForSensor = {
  deviceId: number;
  deviceName: string;
  deviceType: string;
  totalPages: number;
  sensor: Sensor;
};
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
    from = 0;
  }
  if (to === undefined) {
    to = Math.floor(Date.now() / 1000); // default to now
  }
  return await fetchSafe<MeasurementDataForSensor>(
    `${BACKEND_URI}/api/v1/device/measurement/${sensor}?from=${from}&to=${to}&numPage=${page}`,
    addCredentials({})
  );
}

/**
 * Type representing what backend returns when getting notifications.
 */
export interface NotificationEntityQueryReturnType extends NotificationEntity {
  device_id: Device["id"];
  totalPages: number;
}
/**
 * Function for getting the latest notifications. Shouldn't return 404 but empty array.
 */

export async function getLatestNotifications(
  page: number = 0
): Promise<Result<Option<NotificationEntityQueryReturnType[]>, HttpError>> {
  const data = await fetchSafe<NotificationEntityQueryReturnType[]>(
    `${BACKEND_URI}/api/v1/notifications/${page}`,
    addCredentials({})
  );
  console.log(data);
  return data;
}
/**
 * Function for getting all the users. Requires admin privilege.
 */
export async function getUsers(): Promise<Result<Option<User[]>, HttpError>> {
  return await fetchSafe<User[]>(
    `${BACKEND_URI}/api/v1/user/`,
    addCredentials({})
  );
}

/**
 * Function for getting all the users. Requires admin privilege.
 */
export async function deleteUser(
  id: User["id"]
): Promise<Result<Option<User[]>, HttpError>> {
  return await fetchSafe<User[]>(
    `${BACKEND_URI}/api/v1/user/${id}`,
    addCredentials({ method: "DELETE" })
  );
}
/**
 * Registration Request Body JSON format
 */
export type RegistrationRequest = {
  email: string;
  password: string;
  role: ROLE;
  name: string;
  lastName: string;
};
export type RegistrationResponse = {
  email: string;
  role: ROLE;
  name: string;
  lastName: string;
};

export async function registerUser(
  data: RegistrationRequest
): Promise<Result<Option<RegistrationResponse>, HttpError>> {
  return await fetchSafe<RegistrationResponse>(
    `${BACKEND_URI}/auth/register`,
    addCredentials({
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    })
  );
}

export type AnomaliesFetchReturnType = {
  created_at: string;
  type: NotificationEntityType;
};
/**
 * Function for anomalous data from sensor.
 */
export async function getAnomalousData(
  deviceId: Device["id"],
  from?: number,
  to?: number
): Promise<Result<Option<AnomaliesFetchReturnType[]>, HttpError>> {
  if (from === undefined) {
    from = 0; // default to all
  }
  if (to === undefined) {
    to = Math.floor(Date.now() / 1000); // default to now
  }
  const data = await fetchSafe<AnomaliesFetchReturnType[]>(
    `${BACKEND_URI}/api/v1/anomaly/${deviceId}?from=${from}&to=${to}`,
    addCredentials({})
  );

  return data;
}

export async function getMeasurementsForGraph(
  sensorId: Sensor["id"],
  from?: number,
  to?: number,
  max: number = 300
): Promise<Result<Option<MeasurementDataForSensor>, HttpError>> {
  if (from === undefined) {
    from = 0; // default to all
  }
  if (to === undefined) {
    to = Math.floor(Date.now() / 1000); // default to now
  }

  return await fetchSafe<MeasurementDataForSensor>(
    `${BACKEND_URI}/api/v1/device/measurement/graph/${sensorId}?from=${from}&to=${to}&max=${max}`,
    addCredentials({})
  );
}
/**
 * Fetch API implementation that never throws and suggests to check if the data is null.
 */
export async function fetchSafe<T>(
  uri: string,
  headers?: RequestInit
): Promise<Result<Option<T>, HttpError>> {
  try {
    const res = await fetch(uri, headers);
    if (!res.ok) throw HttpError.from_response(res);

    const data: T = await res.json();
    return data;
  } catch (err) {
    return err as HttpError;
  }
}
