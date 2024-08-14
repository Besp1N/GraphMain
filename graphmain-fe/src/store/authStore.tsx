import { createContext, useState, useEffect, ReactNode } from "react";
import {
  ROLE,
  isAuthDataValid,
  getAuthData,
  setAuthData,
  clearAuthData,
  requestLogin,
} from "../http/authUtils";
import { HttpError, Result } from "../http/fetch";

// Defining the shape of the Auth context state
type AuthContextType = {
  role: ROLE | undefined;
  email: string | undefined;
  getToken(): string | undefined;
  login: (
    email: string,
    password: string
  ) => Promise<Result<boolean, HttpError>>;
  logout: () => void;
};

// Creating the context with default values
export const AuthContext = createContext<AuthContextType>({
  role: undefined,
  email: undefined,
  getToken: () => undefined,
  login: async () => false,
  logout: () => undefined,
});

// Provider component that wraps your app and makes auth data available to any child component
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [role, setRole] = useState<ROLE>();
  const [email, setEmail] = useState<string>();

  useEffect(() => {
    const storedAuthData = getAuthData();

    if (storedAuthData && isAuthDataValid(storedAuthData)) {
      setRole(storedAuthData.role);
      setEmail(storedAuthData.email);
    }
  }, []);

  const login = async (
    email: string,
    password: string
  ): Promise<Result<boolean, HttpError>> => {
    const authData = await requestLogin(email, password);
    if (authData instanceof HttpError) {
      return authData;
    }
    if (authData && isAuthDataValid(authData)) {
      setRole(authData.role);
      setEmail(authData.email);
      setAuthData(authData);
    }
    return true;
  };

  const logout = () => {
    setRole(undefined);
    setEmail(undefined);
    clearAuthData();
  };
  const getToken = () => {
    return getAuthData()?.token;
  };

  return (
    <AuthContext.Provider value={{ role, email, login, logout, getToken }}>
      {children}
    </AuthContext.Provider>
  );
};
