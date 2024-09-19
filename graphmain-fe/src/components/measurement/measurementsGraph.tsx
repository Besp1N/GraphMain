import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Cell,
} from "recharts";
import { Sensor } from "../../entities";
import { FC, useCallback, useEffect, useMemo } from "react";
import { useFetchSafe } from "../../http/hooks";
import {
  AnomaliesFetchReturnType,
  getMeasurementsForGraph,
} from "../../http/fetch";
import { MeasurementsFilters } from "./measurmentsFilter";
import Spinner from "../ui/spinner";
import ErrorInfo from "../ui/errorInfo";

interface MeasurementGraphProps {
  sensorId: Sensor["id"];
  anomalies?: AnomaliesFetchReturnType[];
  filters: MeasurementsFilters;
}

const MeasurementGraph: FC<MeasurementGraphProps> = function ({
  sensorId,
  anomalies,
  filters,
}) {
  const cb = useCallback(() => {
    return getMeasurementsForGraph(sensorId, filters.from, filters.to);
  }, [filters, sensorId]);

  const { loading, error, data, fetch } = useFetchSafe(cb);

  useEffect(() => {
    fetch();
  }, [fetch]);

  const measurements = useMemo(
    () =>
      data?.sensor.measurementList?.sort(
        (m1, m2) =>
          new Date(m1.timestamp).getTime() - new Date(m2.timestamp).getTime()
      ),
    [data]
  );

  if (loading) {
    return <Spinner />;
  }
  if (error) {
    return <ErrorInfo title="Failed to create graph" error={error} />;
  }

  if (!measurements?.length || !measurements) {
    return <>Empty data</>;
  }

  // threshold is 1% of graph

  const _threshold =
    (new Date(measurements?.at(-1)?.timestamp)?.getTime() -
      new Date(measurements?.at(0)?.timestamp)?.getTime() ?? Date.now()) * 0.01;
  // Prepare data for the graph
  const isNearAnomaly = (
    timestamp: string,
    anomalies: AnomaliesFetchReturnType[],
    threshold = _threshold
  ) => {
    return anomalies.some((anomaly) => {
      const anomalyTime = new Date(anomaly.created_at).getTime();
      const measurementTime = new Date(timestamp).getTime();
      return Math.abs(anomalyTime - measurementTime) <= threshold; // Check if within threshold (e.g., 1 minute)
    });
  };

  // Sample data structure for the chart
  const graphData = measurements.map((m) => ({
    timestamp: new Date(m.timestamp).toLocaleString(),
    value: m.value,
    anomaly: isNearAnomaly(m.timestamp, anomalies ?? []), // Check if any anomaly is near the measurement timestamp
  }));

  return (
    <ResponsiveContainer width="100%">
      <BarChart
        data={graphData}
        margin={{ top: 40, right: 40, left: 40, bottom: 40 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="timestamp" />
        <YAxis />
        <Tooltip />
        <Bar
          dataKey="value"
          // Color the bar red if it's an anomaly
        >
          {graphData.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={entry.anomaly ? "red" : "blue"} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
};
export default MeasurementGraph;
