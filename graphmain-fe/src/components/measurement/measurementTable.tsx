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
interface IMeasurmentTableProps extends PropsWithChildren {
  title: string;
  measurements: Measurement[];
}
//TODO: split it
const MeasurementTable: FC<IMeasurmentTableProps> = function ({
  title,
  measurements,
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
                    {new Date(measurement.timestap).toLocaleString()}
                  </TableCell>
                  <TableCell align="right">
                    {measurement.id} {measurement.unit}
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
