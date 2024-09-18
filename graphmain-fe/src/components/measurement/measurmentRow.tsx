import { Measurement } from "../../entities";
import { Box } from "@mui/material";

interface IMeasurmentRowProps {
  measurement: Measurement;
  delta?: number;
  anomalous?: boolean;
}

const MeasurementRow = function ({
  measurement,
  delta,
  anomalous,
}: IMeasurmentRowProps) {
  const deltaText: string = delta
    ? `${delta > 0 ? "+" : ""}${delta.toFixed(3)}`
    : "-";
  let deltaColor = "gray";
  if (delta && delta !== 0) {
    deltaColor = delta > 0 ? "green" : "red";
  }
  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      padding="8px 16px"
      minHeight="25%"
      borderBottom="1px solid #ddd"
      color={anomalous ? "1px solid red" : ""}
    >
      <Box flex="1" textAlign="left">
        {new Date(measurement.timestamp).toLocaleString("pl-PL", {
          dateStyle: "short",
          timeStyle: "medium",
        })}
      </Box>
      <Box flex="0 0 auto" color={deltaColor} textAlign="center" width="100px">
        {deltaText}
      </Box>
      <Box flex="0 0 auto" textAlign="right" width="100px">
        {measurement.value}
      </Box>
    </Box>
  );
};

export default MeasurementRow;
