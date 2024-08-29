import { useParams } from "react-router-dom";
import {
  getMeasurements,
  HttpError,
  MeasurementDataForSensor,
} from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { useState, useEffect } from "react";
import { Box, Paper } from "@mui/material"; // Import Pagination component from MUI
import MeasurmentsFilter, {
  MeasurementsFilters,
} from "../measurement/measurmentsFilter";
import InfiniteScroll from "react-infinite-scroll-component";
import MeasurementRow from "../measurement/measurmentRow";
import { Measurement } from "../../entities";
import MeasurementHead from "../measurement/measurementHead";
import { useBreadcrumbs } from "../../store/appStore";

const MeasurementsTablePage = function () {
  useProtectedResource();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_, setBreadcrumbs] = useBreadcrumbs();

  const { deviceId, sensorId } = useParams();
  // Set breadcrumbs once
  useEffect(() => {
    setBreadcrumbs([
      ["All Devices", `/devices`],
      [`Device ${deviceId}`, `/devices/${deviceId}`],
      [`Sensor ${id}`, ""],
    ]);
  }, []);
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

  const { loading, error, data, fetch } = useFetchSafe<
    MeasurementDataForSensor,
    HttpError
  >(async () => {
    const data = await getMeasurements(id, page, filters.from, filters.to);
    if (!data || data instanceof HttpError) {
      setHasMore(false);
      return data;
    }
    setPage((page) => page + 1);
    if (!data.sensor.measurementList?.length) setHasMore(false);
    setMeasurements((m) => [...m, ...(data.sensor?.measurementList ?? [])]);
    return data;
  }, [filters, page, hasMore, measurements]);

  // Always fetch first results to kickstart the stupid scrolls

  useEffect(() => {
    fetch();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  // Render spinner before first data loads
  if (data == null) {
    return (
      <Box mt={4}>
        <Spinner />
      </Box>
    );
  }

  let prevV = measurements?.length ? measurements[0].value : 0;
  return (
    <Box mt={10} maxHeight="80vh">
      <MeasurmentsFilter
        initialValues={filters}
        onFiltersChange={(newFilters) => {
          setPage(0); // Reset page when filters change
          setMeasurements([]); // Clear previous measurements
          setHasMore(true); // Reset hasMore to true
          setFilters(newFilters);
        }}
      />

      {error ? <ErrorInfo error={error} /> : ""}
      {loading && page === 0 ? (
        <Spinner />
      ) : (
        <Box
          display="flex"
          justifyContent="space-around"
          flexWrap={"wrap"}
          mt={4}
          mb={4}
          overflow="auto"
        >
          <Paper
            style={{
              overflowY: "scroll", // Always ensure Y overflow
              maxHeight: "60vh", // Max height
              minHeight: "200px", // Set a minimum height to ensure scrollability
              position: "relative",

              scrollbarWidth: "thin",
            }}
            id="sc-trgt"
          >
            <MeasurementHead
              sensor={data.sensor}
              style={{ position: "sticky", top: 0 }}
            />
            <InfiniteScroll
              dataLength={measurements.length}
              next={fetch}
              hasMore={hasMore}
              loader={<Spinner />}
              scrollThreshold={"20%"}
              style={{ overflow: "hidden" }}
              scrollableTarget="sc-trgt"
              endMessage={<Box>End results</Box>}
            >
              {measurements.map((m) => {
                const delta = m.value - prevV;
                prevV = m.value;
                return (
                  <MeasurementRow
                    key={`${m.id}:${Math.random()}`}
                    measurement={m}
                    delta={delta}
                  />
                );
              })}
            </InfiniteScroll>
          </Paper>
          <Paper>GRAPH HERE</Paper>
        </Box>
      )}
    </Box>
  );
};

export default MeasurementsTablePage;
