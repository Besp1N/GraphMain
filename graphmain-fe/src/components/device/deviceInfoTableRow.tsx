import { TableCell, TableRow } from "@mui/material";
import type { Device } from "../../entities";
import { BACKEND_URI, BackendEndpoint } from "../../http/fetch";
export default function DeviceInfoTableRow({ device }: { device: Device }) {
  return (
    <TableRow>
      <TableCell>{device.id}</TableCell>
      <TableCell>
        <a href={`${BACKEND_URI}/${BackendEndpoint.Device}info/${device.id}`}>
          {device.deviceName}
        </a>
      </TableCell>
      <TableCell>{device.deviceType}</TableCell>
    </TableRow>
  );
}
