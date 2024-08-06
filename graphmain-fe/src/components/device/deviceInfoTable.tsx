import { Device } from "../../entities";
import DeviceInfoTableRow from "./deviceInfoTableRow";
export default function DeviceInfoTable({devices}: {devices: Device[]}) {
    return(
        devices.map(device => <DeviceInfoTableRow key={device.id} device={device} />)
    );
}