import { useState, useEffect } from 'react';
import { Result, Option } from './fetch';
/**
 *  Hook that manages loading and errors while fetching data.
 * Important: if the function depends on data from a component, it
 * needs to be wrapped in useCallback to prevent runaway loop 
 */
export function useFetchSafe<T>(fn: () => Promise<Option<Result<T>>>) {

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<Error | undefined>();
    const [data, setData] = useState<T | undefined>();

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

    return { loading, error, data };
}
