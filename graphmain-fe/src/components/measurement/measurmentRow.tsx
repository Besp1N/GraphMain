import { Measurement } from "../../entities";
import { TableCell, TableRow } from "@mui/material";
interface IMeasurmentRowProps {
  measurement: Measurement;
  delta?: number;
}
const MeasurementRow = function ({ measurement, delta }: IMeasurmentRowProps) {
  const deltaText: string = delta
    ? `${delta > 0 ? "+" : ""}${delta.toFixed(3)}`
    : "-";
  let deltaColor = "gray";
  // if (delta && delta != 0) {
  //   deltaColor = delta && delta > 0 ? "green" : "red";
  // }
  return (
    <TableRow>
      <TableCell align="left">
        {new Date(measurement.timestamp).toLocaleString("pl-PL", {
          dateStyle: "short",
          timeStyle: "medium",
        })}
      </TableCell>
      <TableCell style={{ color: deltaColor }}>{deltaText}</TableCell>

      <TableCell align="right">{measurement.value}</TableCell>
    </TableRow>
  );
};
export default MeasurementRow;
