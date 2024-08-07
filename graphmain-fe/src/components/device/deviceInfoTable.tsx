import { Table, TableBody } from "@mui/material";
import { Device } from "../../entities";
import DeviceInfoTableRow from "./deviceInfoTableRow";
export default function DeviceInfoTable({devices}: {devices: Device[]}) {
    return(
        <Table>
            <TableBody>
            {devices.map(device => <DeviceInfoTableRow key={device.id} device={device} />)}
            </TableBody>
        </Table>
    );
}