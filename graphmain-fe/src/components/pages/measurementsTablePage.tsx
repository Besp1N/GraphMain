import { useParams } from "react-router-dom";
import {
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
import InfiniteScrollWrapper, { MeasurementsScrollImperativeHandle } from "../measurement/measurementInfiniteScroll";
import { Sensor } from "../../entities";
import MeasurementGraph from "../measurement/measurementsGraph";

const MeasurementsTablePage = function () {
  useProtectedResource();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_, setBreadcrumbs] = useBreadcrumbs();

  const { deviceId, sensorId } = useParams();
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
  const [anomalies, setAnomalies] = useState<Sensor["id"][]>([]);
  const [filters, setFilters] = useState<MeasurementsFilters>({
    to: undefined,
    from: undefined,
  });

  useEffect(() => {
    (async () => {
      const data = await getAnomalousData(id);
      if (data == undefined || data instanceof HttpError) console.error(data);
      else setAnomalies(data!);

    })();
  }, [filters]);


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
        flexDirection={{ xs: "column", md: "row" }}  // Column on small screens, row on medium and up
        flexWrap="wrap"
        mt={4}
        mb={4}
        maxHeight="80vh"
        width="100%"
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
          <InfiniteScrollWrapper id={id} filters={filters} ref={scrollRef} anomalies={anomalies} />
        </Paper>

        <Box width={{ xs: "100%", md: "800px" }} maxHeight="80vh" mt={{ xs: 2, md: 0 }}>
          <MeasurementGraph sensorId={id} filters={filters} anomalies={anomalies} />
        </Box>
      </Box>
    </>
  );
};

export default MeasurementsTablePage;
