import { useState, useEffect } from 'react';
import { Result, Option } from './fetch';
import { getAuthData, ROLE } from './authUtils';
import { useNavigate } from 'react-router-dom';
import { useFlash } from '../store/flashStore';
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

export function useProtectedResource(role?: ROLE) {
    const navigate = useNavigate();
    const {addFlash} = useFlash();
    useEffect(() => {
        const authData = getAuthData();
        if (!authData || !authData?.role || (role && (authData?.role !== role))) {
            addFlash("error", "You have to be logged in to access this resource.")
            navigate("/login");
        }
    }, [role, navigate, addFlash])
}

