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

interface IMeasurementTableProps extends PropsWithChildren {
  title: string;
  measurements: Measurement[];
}

const MeasurementTable: FC<IMeasurementTableProps> = function (
    {
        title,
        measurements = [],
    }) {
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
                <TableCell align="right">
                  <strong>Measurement (Unit)</strong>
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {measurements.length > 0 ? (
                  measurements.map((measurement) => (
                      <TableRow key={measurement.id}>
                        <TableCell>
                          {new Date(measurement.timestamp).toLocaleString("pl-PL", {
                            dateStyle: "short",
                            timeStyle: "medium",
                          })}
                        </TableCell>
                        <TableCell align="right">
                          {measurement.value} {measurement.unit}
                        </TableCell>
                      </TableRow>
                  ))
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
