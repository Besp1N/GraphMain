import { useParams } from "react-router-dom";
import {
  AnomaliesFetchReturnType,
  getAnomalousData,
  HttpError,
} from "../../http/fetch";
import { useProtectedResource } from "../../http/hooks";

import { useState, useEffect, useRef } from "react";
import { Box, Paper } from "@mui/material"; // Import Pagination component from MUI
import MeasurmentsFilter, {
  MeasurementsFilters,
} from "../measurement/measurmentsFilter";
import { useBreadcrumbs } from "../../store/appStore";
import InfiniteScrollWrapper, {
  MeasurementsScrollImperativeHandle,
} from "../measurement/measurementInfiniteScroll";
import MeasurementGraph from "../measurement/measurementsGraph";

const MeasurementsTablePage = function () {
  useProtectedResource();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_, setBreadcrumbs] = useBreadcrumbs();

  const { deviceId, sensorId } = useParams();
  // Ref to control InfiniteScroll
  const scrollRef = useRef<MeasurementsScrollImperativeHandle>();
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
  const [anomalies, setAnomalies] = useState<AnomaliesFetchReturnType[]>([]);
  const [filters, setFilters] = useState<MeasurementsFilters>({
    to: undefined,
    from: undefined,
  });

  // Fetch anomalies only when filters change
  useEffect(() => {
    (async () => {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      //@ts-expect-error
      const data = await getAnomalousData(+deviceId);
      console.log(data);
      if (data == undefined || data instanceof HttpError) console.error(data);
      else setAnomalies(data!);
    })();
  }, [id, filters]);

  return (
    <>
      <MeasurmentsFilter
        initialValues={filters}
        onFiltersChange={(newFilters) => {
          scrollRef.current?.reset();
          setFilters(newFilters);
        }}
      />
      <Box
        display="flex"
        justifyContent="space-around"
        flexDirection={{ xs: "column", s: "column", md: "row" }} // Column on small screens, row on medium and up
        flexWrap="nowrap"
        mt={4}
        mb={4}
        maxHeight="80vh"
        width="100%"
        position="relative"
      >
        <Paper
          style={{
            overflowY: "scroll",
            maxHeight: "80vh",
            minHeight: "200px",
            position: "relative",
            scrollbarWidth: "thin",
          }}
          id="sc-trgt"
        >
          <InfiniteScrollWrapper
            id={id}
            filters={filters}
            ref={scrollRef}
            // anomalies={anomalies}
          />
        </Paper>

        <Box
          width={{ xs: "100%", s: "100%", md: "calc(100% - 20px)" }} // Full width on mobile
          height={{ xs: "70vh", s: "70vh" }}
          maxWidth="1000px"
          maxHeight="80vh"
          mt={{ xs: 2, s: 2, md: 0 }}
          position="relative"
          overflow="hidden" // Prevents content from overflowing
          padding={{ xs: "0", md: "16px" }} // No padding on mobile
          margin={{ xs: "0", md: "auto" }} // Removes margin on mobile to prevent shifting
        >
          <MeasurementGraph
            sensorId={id}
            filters={filters}
            anomalies={anomalies}
          />
        </Box>
      </Box>
    </>
  );
};

export default MeasurementsTablePage;
