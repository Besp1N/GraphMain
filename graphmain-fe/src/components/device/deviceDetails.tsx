import { Device } from "../../entities";
/**
 * React Component responsible for displaying details about a device
 */
export default function DeviceInfoTable({ device }: { device: Device }) {
  return <div>{device.deviceName}</div>;
}
