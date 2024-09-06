import { ResponsiveLine } from "@nivo/line";
import { Sensor, Measurement } from "../../entities";
import { FC, useCallback, useEffect } from "react";
import { useFetchSafe } from "../../http/hooks";
import { getMeasurementsForGraph } from "../../http/fetch";
import { MeasurementsFilters } from "./measurmentsFilter";
import Spinner from "../ui/spinner";
import ErrorInfo from "../ui/errorInfo";

interface MeasurementGraphProps {
  sensorId: Sensor["id"];
  anomalies?: Measurement["id"][];
  filters: MeasurementsFilters;
}

const MeasurementGraph: FC<MeasurementGraphProps> = function ({
  sensorId,
  anomalies,
  filters,
}) {
  const anomalySet = new Set(anomalies);

  const cb = useCallback(() => {
    return getMeasurementsForGraph(sensorId, filters.from, filters.to);
  }, [filters, sensorId]);

  const { loading, error, data, fetch } = useFetchSafe(cb);

  useEffect(() => {
    fetch();
  }, [fetch]);

  if (loading) {
    return <Spinner />;
  }
  if (error) {
    return <ErrorInfo title="Failed to create graph" error={error} />;
  }
  const measurements = data?.sensor?.measurementList;
  if (!measurements?.length) {
    return <>Empty data</>;
  }
  // Prepare data for the graph
  let min = measurements[0].value;
  let max = measurements[0].value;
  const graphData = [
    {
      id: data?.sensor.sensorName ?? "No Data", // Use sensor name or fallback
      data: measurements.map((m) => {
        if (m.value > max) max = m.value;
        if (m.value < min) min = m.value;
        return {
          x: new Date(m.timestamp), // Ensure timestamp is parsed as Date
          y: m.value,
          anomaly: anomalySet.has(m.id), // Mark as anomaly if in set
        };
      }),
    },
  ];

  return (
    <ResponsiveLine
      data={graphData}
      margin={{ right: 110, left: 60, bottom: 40 }}
      xScale={{
        type: "time",
        format: "%Y-%m-%dT%H:%M:%S.",
        precision: "millisecond",
      }} // Time scale
      xFormat="time:%Y-%m-%d %H:%M:%S"
      yScale={{
        type: "linear",
        min,
        max,
        stacked: false,
        reverse: false,
      }}
      axisBottom={{
        format: "%b %d, %H:%M", // Format for the x-axis
        tickValues: 5,

        legendOffset: 36,
        legendPosition: "middle",
      }}
      axisLeft={{
        legend: data?.sensor.unit ?? "Value", // Show unit if available
        legendOffset: -40,
        legendPosition: "middle",
      }}
      lineWidth={3}
      pointSize={10}
      pointColor={{ theme: "background" }}
      pointBorderWidth={2}
      // pointBorderColor={({ datum }: {datum: Datum}) => (datum.anomaly ? "red" : "blue")} // Color based on anomaly
      //   enablePointLabel={true}
      pointLabel="y"
      pointLabelYOffset={-12}
      enableGridX={false}
      enableGridY={true}
      useMesh={true}
      tooltip={({ point }) => (
        <div
          style={{
            background: "white",
            padding: "6px 12px",
            border: `1px solid ${point.borderColor}`,
          }}
        >
          <strong>{point.data.xFormatted}</strong>
          <br />
          Value: {point.data.y}
          <br />
          {point.data?.anomaly && (
            <span style={{ color: "red" }}>Anomaly!</span>
          )}
        </div>
      )}
      legends={[
        {
          anchor: "bottom-right",
          direction: "column",
          justify: false,
          translateX: 100,
          translateY: 0,
          itemsSpacing: 0,
          itemDirection: "left-to-right",
          itemWidth: 80,
          itemHeight: 20,
          itemOpacity: 0.75,
          symbolSize: 12,
          symbolShape: "circle",
          symbolBorderColor: "rgba(0, 0, 0, .5)",
          effects: [
            {
              on: "hover",
              style: {
                itemBackground: "rgba(0, 0, 0, .03)",
                itemOpacity: 1,
              },
            },
          ],
        },
      ]}
    />
  );
};

export default MeasurementGraph;
