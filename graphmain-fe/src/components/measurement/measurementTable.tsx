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
  measurements?: Measurement[];
  anomalies?: Measurement["id"][]
}

const MeasurementTable: FC<IMeasurementTableProps> = function ({
  title,
  unit,
  measurements = [],
  children,
  ...props
}) {
  let previousMeasurmentValue = 0.0;
  return (
    <Container
      maxWidth="md"
      style={{ marginTop: "20px", maxHeight: "80vh", overflow: "auto" }}
      {...props}
    >
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
            {measurements.length > 0
              ? measurements.map((measurement, i) => {
                  const delta = previousMeasurmentValue - measurement.value;
                  previousMeasurmentValue = measurement.value;
                  return (
                    <MeasurementRow
                      key={measurement.id}
                      measurement={measurement}
                      delta={i !== 0 ? delta : 0}
                    />
                  );
                })
              : children}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default MeasurementTable;
