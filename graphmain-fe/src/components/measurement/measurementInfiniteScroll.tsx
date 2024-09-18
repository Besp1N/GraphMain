import {
  useState,
  useEffect,
  useImperativeHandle,
  useCallback,
  forwardRef,
} from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import Spinner from "../ui/spinner";
import MeasurementRow from "./measurmentRow";
import { Measurement, Sensor } from "../../entities";
import { useFetchSafe } from "../../http/hooks";
import {
  getMeasurements,
  HttpError,
  MeasurementDataForSensor,
} from "../../http/fetch";
import { Box } from "@mui/material";
import MeasurementHead from "./measurementHead";
import { MeasurementsFilters } from "./measurmentsFilter";

/**
 * Interface exposed by ref for MeasurementInfiniteScroll
 * reset() resets the scroll.
 */
export interface MeasurementsScrollImperativeHandle {
  page: number;
  measurements: Measurement[];
  reset: () => void;
  error: Error;
  loading: boolean;

  sensor: Sensor;
}
interface MeasurementInfiniteScrollProps {
  filters: MeasurementsFilters;
  id: Sensor["id"];
  anomalies?: Measurement[];
}

const MeasurementInfiniteScroll = forwardRef(
  ({ id, filters, anomalies }: MeasurementInfiniteScrollProps, ref) => {
    const [page, setPage] = useState(0); // Add page state
    const [measurements, setMeasurements] = useState<Measurement[]>([]); // Store all loaded measurements
    const [hasMore, setHasMore] = useState(true); // Track if more data is available

    const { error, data, fetch } = useFetchSafe<
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

    // Reset function to reset state
    const reset = useCallback(() => {
      setPage(0);
      setMeasurements([]);
      setHasMore(true);
    }, []);

    // Expose methods and state through the ref
    useImperativeHandle(ref, () => ({
      page,
      measurements,
      error,
      sensor: data?.sensor,
      reset,
    }));

    useEffect(() => {
      fetch();
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [filters]);

    if (data == null) {
      return (
        <Box mt={4}>
          <Spinner />
        </Box>
      );
    }
    // Create a set for quick lookup
    const anomalySet = new Set(anomalies?.map((m) => m.id));
    let prevV = measurements?.length ? measurements[0].value : 0;

    return (
      <>
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
                anomalous={anomalySet.has(m.id)}
              />
            );
          })}
        </InfiniteScroll>
      </>
    );
  }
);
export default MeasurementInfiniteScroll;
