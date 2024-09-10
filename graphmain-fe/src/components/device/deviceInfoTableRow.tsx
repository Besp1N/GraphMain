import { TableCell, TableRow } from "@mui/material";
import type { Device } from "../../entities";
import { Link } from "react-router-dom";

export default function DeviceInfoTableRow({ device }: { device: Device }) {
  return (
    <TableRow>
      <TableCell>{device.id}</TableCell>
      <TableCell style={{ textWrap: "wrap" }}>
        <Link to={`/devices/${device.id}`}>{device.deviceName}</Link>
      </TableCell>
      <TableCell>{device.deviceType}</TableCell>
    </TableRow>
  );
}
