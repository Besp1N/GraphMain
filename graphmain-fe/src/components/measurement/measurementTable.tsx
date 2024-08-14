import {
  Container,
  Typography,
  TableContainer,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from "@mui/material";
import { FC, PropsWithChildren } from "react";
import { Measurement } from "../../entities";
import MeasurementRow from "./measurmentRow";

interface IMeasurementTableProps extends PropsWithChildren {
  title: string;
  unit: string;
  measurements: Measurement[];
}

const MeasurementTable: FC<IMeasurementTableProps> = function ({
  title,
  unit,
  measurements = [],
}) {
  let previousMeasurmentValue = 0.0;
  return (
    <Container maxWidth="md" style={{ marginTop: "20px" }}>
      <Typography variant="h5" component="div" gutterBottom>
        {title}
      </Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <strong>Timestamp</strong>
              </TableCell>
              <TableCell>
                <strong>Delta from previous</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Measurement ({unit})</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {measurements.length > 0 ? (
              measurements.map((measurement) => {
                const delta = previousMeasurmentValue - measurement.value;
                previousMeasurmentValue = measurement.value;
                return (
                  <MeasurementRow
                    key={measurement.id}
                    measurement={measurement}
                    delta={delta}
                  />
                );
              })
            ) : (
              <TableRow>
                <TableCell colSpan={2} align="center">
                  <Typography variant="body2" color="textSecondary">
                    No measurements available.
                  </Typography>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default MeasurementTable;
