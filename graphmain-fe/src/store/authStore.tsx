import {
  createContext,
  useState,
  useEffect,
  ReactNode,
  useContext,
} from "react";
import {
  ROLE,
  isAuthDataValid,
  getAuthData,
  setAuthData,
  clearAuthData,
  requestLogin,
} from "../http/authUtils";
import { HttpError, Result } from "../http/fetch";
import { AppContext } from "./appStore";

// Defining the shape of the Auth context state
type AuthContextType = {
  loggedIn: boolean;
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
  loggedIn: false,
  role: undefined,
  email: undefined,
  getToken: () => undefined,
  login: async () => false,
  logout: () => undefined,
});

// Provider component that wraps your app and makes auth data available to any child component
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const prevAuthData = getAuthData();
  const [role, setRole] = useState<ROLE | undefined>(prevAuthData?.role);
  const [email, setEmail] = useState<string>(prevAuthData?.email ?? "");
  const [loggedIn, setLoggedIn] = useState<boolean>(!!prevAuthData?.role);
  useEffect(() => {
    const storedAuthData = getAuthData();

    if (storedAuthData && isAuthDataValid(storedAuthData)) {
      setRole(storedAuthData.role);
      setEmail(storedAuthData.email);
      setLoggedIn(true);
    }
  }, []);
  const { connectWebSocket, disconnectWebSocket } = useContext(AppContext)!;
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
      setLoggedIn(true);
      setAuthData(authData);
      connectWebSocket(authData.token);
    }
    return true;
  };

  const logout = () => {
    setRole(undefined);
    setEmail("");
    setLoggedIn(false);
    clearAuthData();
    disconnectWebSocket();
  };
  const getToken = () => {
    return getAuthData()?.token;
  };

  return (
    <AuthContext.Provider
      value={{ loggedIn, role, email, login, logout, getToken }}
    >
      {children}
    </AuthContext.Provider>
  );
};
