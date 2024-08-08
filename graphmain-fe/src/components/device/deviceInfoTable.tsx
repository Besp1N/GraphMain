import {
  Table,
  TableBody,
  TableCell,
  TableRow,
  TableHead,
} from "@mui/material";
import { Device } from "../../entities";
import DeviceInfoTableRow from "./deviceInfoTableRow";
/**
 * React Component responsible for displaying a list of devices in a from of a table.
 */
export default function DeviceInfoTable({ devices }: { devices: Device[] }) {
  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>
            <b>Device Id</b>
          </TableCell>
          <TableCell>
            <b>Device Name</b>
          </TableCell>
          <TableCell>
            <b>Device Type</b>
          </TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {devices.map((device) => (
          <DeviceInfoTableRow key={device.id} device={device} />
        ))}
      </TableBody>
    </Table>
  );
}
