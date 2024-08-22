import { redirect, useParams } from "react-router-dom";
import { getMeasurements } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import MeasurementTable from "../measurement/measurementTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { useState, useCallback, useEffect } from "react";
import Pagination from "@mui/material/Pagination";
import { Box } from "@mui/material"; // Import Pagination component from MUI

const MeasurementsTablePage = function () {
  useProtectedResource();

  const { sensorId } = useParams();
  const id = parseInt(sensorId!);

  if (Number.isNaN(id)) {
    redirect("home"); // Should redirect to 404
  }
  const [page, setPage] = useState(0);

  const fetchMeasurements = useCallback(
    () => getMeasurements(id, page),
    [id, page]
  );

  const { loading, error, data, fetch } = useFetchSafe(fetchMeasurements);
  useEffect(() => fetch(), [fetch]);

  const handlePageChange = (
    _event: React.ChangeEvent<unknown>,
    value: number
  ) => {
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

  const measurements = data.sensor.measurementList ?? [];
  const totalPages = data.totalPages ?? 0;
  return (
    <>
      <MeasurementTable
        measurements={measurements}
        title="All recent measurements"
        unit={data.sensor.unit}
      />
      <Box display="flex" justifyContent="center" mt={4}>
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
