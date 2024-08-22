import { useParams } from "react-router-dom";
import { getMeasurements, HttpError } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { useState, useEffect } from "react";
import { Box, Container, Paper, TableCell, TableRow } from "@mui/material"; // Import Pagination component from MUI
import Breadcrumbs from "../ui/breadcrumbs";
import MeasurmentsFilter, {
  MeasurementsFilters,
} from "../measurement/measurmentsFilter";
import InfiniteScroll from "react-infinite-scroll-component";
import MeasurementRow from "../measurement/measurmentRow";
import { Measurement } from "../../entities";

const MeasurementsTablePage = function () {
  useProtectedResource();

  const { sensorId } = useParams();
  const id = parseInt(sensorId!);

  if (Number.isNaN(id)) {
    throw new HttpError(
      "The page for sensor " + sensorId + " doesn't exist.",
      404
    );
  }

  const [filters, setFilters] = useState<MeasurementsFilters>({
    to: undefined,
    from: undefined,
  });

  const [page, setPage] = useState(0); // Add page state
  const [hasMore, setHasMore] = useState(true); // Track if more data is available
  const [measurements, setMeasurements] = useState<Measurement[]>([]); // Store all loaded measurements

  const { loading, error, data, fetch } = useFetchSafe(async () => {
    const response = await getMeasurements(id, page, filters.from, filters.to);
    if (response instanceof HttpError) return undefined;
    if (response!.sensor.measurementList.length === 0) {
      setHasMore(false); // If no more measurements, stop further fetching
    } else {
      setMeasurements((prev) => [...prev, ...response!.sensor.measurementList]); // Append new measurements
    }

    return response;
  }, [filters, id, page]);
  //First fetch
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(fetch, [filters, id, page, hasMore]);

  const loadMore = () => {
    if (!loading && hasMore) {
      setPage((prevPage) => prevPage + 1); // Increment page number
    }
  };

  if (measurements == null || data == null) {
    return (
      <Container>
        <Spinner />
      </Container>
    );
  }
  let prevV = measurements[0].value;
  return (
    <>
      <Breadcrumbs
        breadcrumbs={[
          ["All Devices", `/devices`],
          [
            `Device ${data.deviceId} : ${data.deviceName}`,
            `/devices/${data.deviceId}`,
          ],
          [`Sensor ${id} : ${data.sensor.sensorName}`, ""],
        ]}
      />
      <MeasurmentsFilter
        initialValues={filters}
        onFiltersChange={(newFilters) => {
          setPage(0); // Reset page when filters change
          setMeasurements([]); // Clear previous measurements
          setHasMore(true); // Reset hasMore to true
          setFilters((prevFilters) =>
            prevFilters.from !== newFilters.from ||
            prevFilters.to !== newFilters.to
              ? newFilters
              : prevFilters
          );
        }}
      />

      {error ? <ErrorInfo error={error} /> : ""}
      {loading && page === 0 ? (
        <Spinner />
      ) : (
        <Box
          display="flex"
          justifyContent="space-around"
          mt={4}
          overflow="auto"
        >
          <Paper style={{ overflow: "auto", maxHeight: "60vh" }} id="sc-trgt">
            <InfiniteScroll
              dataLength={measurements.length}
              next={loadMore}
              hasMore={hasMore}
              loader={<Spinner />}
              style={{ overflow: "hidden" }}
              scrollableTarget="sc-trgt"
              endMessage={
                <TableRow>
                  <TableCell>End results</TableCell>
                </TableRow>
              }
            >
              {measurements.map((m) => {
                const delta = m.value - prevV;
                prevV = m.value;
                return (
                  <MeasurementRow key={m.id} measurement={m} delta={delta} />
                );
              })}
            </InfiniteScroll>
          </Paper>
          <Paper>GRAPH HERE</Paper>
        </Box>
      )}
    </>
  );
};

export default MeasurementsTablePage;
