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
import { NotificationEntityType, Sensor } from "../../entities";
import { FC, useCallback, useEffect, useMemo, useState } from "react";
import { useFetchSafe } from "../../http/hooks";
import {
  AnomaliesFetchReturnType,
  getAnomalousData,
  getMeasurementsForGraph,
  HttpError,
} from "../../http/fetch";
import { MeasurementsFilters } from "./measurmentsFilter";
import Spinner from "../ui/spinner";
import ErrorInfo from "../ui/errorInfo";

type GraphData = {
  timestamp: string;
  value: number | null;
  anomaly: NotificationEntityType | null;
  gap: boolean;
};

function anomalyTypeToColor(anomalyType: NotificationEntityType): string {
  switch (anomalyType) {
    case "error":
      return "red";
    case "warning":
      return "orange";
    default:
      return "blue";
  }
}

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
  const getNearAnomaly = (
    timestamp: string,
    anomalies: AnomaliesFetchReturnType[],
    threshold = _threshold
  ) => {
    const anomaly = anomalies.find((anomaly) => {
      const anomalyTime = new Date(anomaly.created_at).getTime();
      const measurementTime = new Date(timestamp).getTime();
      return Math.abs(anomalyTime - measurementTime) <= threshold;
    });
    return anomaly ? anomaly.type : null;
  };

  const graphData: GraphData[] = [];
  measurements.forEach((m, idx) => {
    const currentTimestamp = new Date(m.timestamp).getTime();
    const previousTimestamp =
      idx > 0 ? new Date(measurements[idx - 1].timestamp).getTime() : null;

    // Check if there's a gap
    const gap =
      previousTimestamp !== null &&
      currentTimestamp - previousTimestamp > _threshold;

    // Insert a blank entry before the current one if there's a gap
    if (gap) {
      graphData.push({
        timestamp: "", // Empty timestamp to create a blank space
        value: null, // No value for the gap
        anomaly: null, // No anomaly for the gap
        gap: true, // Mark it as a gap
      });
    }

    const formattedTimestamp = new Date(m.timestamp).toLocaleString("en-US", {
      year: "2-digit",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false, // Change to true if you prefer 12-hour format with AM/PM
    });
    // Add the current data entry
    graphData.push({
      timestamp: formattedTimestamp,
      value: m.value,
      anomaly: getNearAnomaly(m.timestamp, anomalies ?? []),
      gap: false,
    });
  });

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
            <Cell
              key={`cell-${index}`}
              fill={
                entry.anomaly
                  ? anomalyTypeToColor(entry.anomaly)
                  : entry.gap
                  ? "transparent" // Blank cell color for gaps
                  : "blue"
              }
            />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
};
export default MeasurementGraph;
export type Period = "week" | "day" | "hour";
/**
 * Returns epoch timestamp in seconds to the last period of time
 *
 */
function timestampFromPeriod(p: Period): number {
  switch (p) {
    case "week":
      return Math.floor(Date.now() / 1000 - 60 * 60 * 24 * 7);
    case "day":
      return Math.floor(Date.now() / 1000 - 60 * 60 * 24);
    case "hour":
      return Date.now() / 1000 - 60 * 60;
    // default to last week
    default:
      return Math.floor(Date.now() / 1000 - 60 * 60 * 24 * 7);
  }
}
export function LastPeriodMeasurementGraph({
  sensorId,
  period,
}: {
  sensorId: number;
  period: Period;
}) {
  const filters: MeasurementsFilters = {
    from: timestampFromPeriod(period),
    to: Math.floor(Date.now() / 1000),
  };
  const [anomalies, setAnomalies] = useState<AnomaliesFetchReturnType[]>([]);
  useEffect(() => {
    (async () => {
      const data = await getAnomalousData(3);
      if (!data || data instanceof HttpError) {
        return;
      }
      setAnomalies(data);
    })();
  }, []);

  return (
    <MeasurementGraph
      sensorId={sensorId}
      filters={filters}
      anomalies={anomalies}
    />
  );
}
