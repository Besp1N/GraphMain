import { TableCell, TableRow } from "@mui/material";
import type { Device } from "../../entities";
export default function DeviceInfoTableRow({device}: {device: Device}) {
    return (
        <TableRow>
            <TableCell>
                {device.deviceName}
            </TableCell>
            <TableCell>
                {device.deviceType}
            </TableCell>
            <TableCell>
                {device.sensors}
                TODO:GET THEIR NAMES AND DISPLAY
            </TableCell>
        </TableRow>
    );
}