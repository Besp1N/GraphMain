import { useState, useEffect } from 'react';
import { Result, Option } from './fetch';
import { getAuthData, ROLE } from './authUtils';
import { useNavigate } from 'react-router-dom';
/**
 *  Hook that manages loading and errors while fetching data.
 * Important: if the function depends on data from a component, it
 * needs to be wrapped in useCallback to prevent runaway loop 
 */
export function useFetchSafe<T, E extends Error>(fn: () =>  Promise<Result<Option<T>, E>>): {
    loading: boolean,
    error: Option<E>,
    data: Option<T>
} {

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<Option<E>>();
    const [data, setData] = useState<Option<T>>();

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const result = await fn();
            if (result instanceof Error ) {
                setError(result);
            }
            else {
                setData(result);
                setError(undefined);
            }
            setLoading(false);

        };

        fetchData();
    }, [fn]);

    return { loading, error, data: data as Option<T>  };
}

export function useProtectedResource(role: ROLE) {
    const navigate = useNavigate();
    useEffect(() => {
        const authData = getAuthData();
        if (authData?.role == role) return;
        navigate("/login");

    }, [role, navigate])
}