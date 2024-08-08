import { useParams } from "react-router-dom";
import { getMeasurements } from "../../http/fetch";
import { useFetchSafe } from "../../http/hooks";
import MeasurementTable from "../measurement/measurementTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { useState, useCallback } from "react";
import Pagination from '@mui/material/Pagination';
import {Box} from "@mui/material"; // Import Pagination component from MUI

const MeasurementsTablePage = function () {
  const { sensorId: id } = useParams();
  const [page, setPage] = useState(0);
  const [from, setFrom] = useState<number | undefined>(undefined);
  const [to, setTo] = useState<number | undefined>(undefined);

  const fetchMeasurements = useCallback(() => getMeasurements(+id, page, from, to), [id, page, from, to]);

  const { loading, error, data } = useFetchSafe(fetchMeasurements);

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // Pagination in MUI is 1-based, but we use 0-based indexing for pages
  };

  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorInfo error={error} />;
  }

  if (data == null) {
    return <p>Should be 404</p>;
  }

  console.log('Data received:', data); // Debugging line

  const measurements = data.measurements ?? [];
  const totalPages = data.totalPages ?? 0;

  return (
      <>
        <MeasurementTable
            measurements={measurements}
            title="All recent measurements"
        />
        <Box
            display="flex"
            justifyContent="center"
            mt={4}
        >
          <Pagination
              count={totalPages}
              page={page + 1}
              onChange={handlePageChange}
              color="primary"
          />
        </Box>
      </>
  );
};

export default MeasurementsTablePage;
