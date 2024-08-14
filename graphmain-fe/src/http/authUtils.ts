import { fetchSafe, HttpError, Result, Option } from "./fetch";
export const BACKEND_URI = "http://127.0.0.1:8080";

const LOGIN_URL = `${BACKEND_URI}/auth/login`;
export const LOCALSTORAGE_AUTHDATA_NAME = "auth_data";
export type AuthData = {
    role: ROLE,
    email: string,
    token: string
}
export enum ROLE {
    ADMIN = "ADMIN",
    USER = "USER",
} 
const ROLES_ARRAY = [ROLE.ADMIN, ROLE.USER];

/**
 * Helper function checking if authdata is valid - has role, email, token, isn't null etc.
 * @param authData 
 */
export function isAuthDataValid(authData: AuthData): boolean {
    return !!(authData && typeof authData == "object" && ROLES_ARRAY.includes(authData?.role) && authData?.email && authData?.token);
}
export function getToken(): Option<string> {
    return getAuthData()?.token;
}

export function getAuthData(): Option<AuthData> {
    const data = localStorage.getItem(LOCALSTORAGE_AUTHDATA_NAME);
    if (!data) return undefined;
    // wrapping in try catch while parsing
    try {
        const authData = JSON.parse(data);
        // If authData isn't null and has role, email and token we can return it.
        if (isAuthDataValid(authData))
            return authData;
        else throw new Error("Invalid Auth Data in localstorage. This incident will be reported.");
    }
    catch {/*pass and return undefined */ }
    return undefined;
}
export function setAuthData(authData: AuthData): Result<undefined, Error> {
    if (isAuthDataValid(authData)) {
        localStorage.setItem(LOCALSTORAGE_AUTHDATA_NAME, JSON.stringify(authData));
        return undefined;
    }
    return new Error(`Cannot set provided authData to local storage:\nGot ${authData}`);
}
export function clearAuthData() {
    localStorage.removeItem(LOCALSTORAGE_AUTHDATA_NAME);
}
export async function requestLogin(email: string, password: string): Promise<Result<AuthData, HttpError>> {
    const res = await fetchSafe<AuthData>(LOGIN_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email,
            password
        })
    
    });
    if (res instanceof HttpError ) {
        return res;
    }
 
    if (res === undefined || !isAuthDataValid(res)) return new HttpError("Something went wrong with the authentication response", 500);
    return res!;
}
