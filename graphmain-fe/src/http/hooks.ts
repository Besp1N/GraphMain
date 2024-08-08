import { useState, useEffect } from 'react';
/**
 *  Hook that manages loading and errors while fetching data. 
 */
export function useFetch<T>(endpoint: URL) {
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<Error | undefined>();
    const [data, setData] = useState<T | undefined>();

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await fetch(endpoint.toString());
                if (!response.ok) {
                    throw new Error(`Error: ${response.statusText}`);
                }
                const result = await response.json();
                setData(result);
            } catch (err) {
                setError(err as Error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [endpoint]);

    return { loading, error, data };
}
