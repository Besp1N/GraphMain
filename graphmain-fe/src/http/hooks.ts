import { useState, useEffect, useCallback, useContext } from "react";
import { Result, Option, HttpError } from "./fetch";
import { getAuthData, ROLE } from "./authUtils";
import { useNavigate } from "react-router-dom";
import { useFlash } from "../store/flashStore";
import { AuthContext } from "../store/authStore";
/**
 *  Hook that manages loading and errors while fetching data.
 * Important: if the function depends on data from a component, it
 * needs to be wrapped in useCallback to prevent runaway loop
 */
export function useFetchSafe<T, E extends Error>(
  fn: () => Promise<Result<Option<T>, E>>,
  deps?: unknown[]
): {
  loading: boolean;
  error: Option<E>;
  data: Option<T>;
  fetch: () => void;
} {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<Option<E>>();
  const [data, setData] = useState<Option<T>>();
  const navigate = useNavigate();
  const flash = useFlash();
  const { logout } = useContext(AuthContext);
  const fetch = useCallback(
    () => {
      const fetchData = async () => {
        setLoading(true);
        const result = await fn();
        if (result instanceof Error) {
          setError(result);
          if (result instanceof HttpError && result.statusCode === 401) {
            flash.addFlash("warning", "You are currently logged out.");
            logout();
            navigate("/");
          }
        } else {
          setData(result);
          setError(undefined);
        }
        setLoading(false);
      };

      fetchData();
      // eslint-disable-next-line react-hooks/exhaustive-deps
    },
    deps ? [fn, navigate, ...deps] : [fn, navigate]
  );

  return { loading, error, data: data as Option<T>, fetch };
}

export function useProtectedResource(role?: ROLE) {
  const navigate = useNavigate();
  const { addFlash } = useFlash();
  useEffect(() => {
    const authData = getAuthData();
    if (!authData || !authData?.role || (role && authData?.role !== role)) {
      addFlash("error", "You have to be logged in to access this resource.");
      navigate("/login");
    }
  }, [role, navigate, addFlash]);
}
