import { useEffect, useState } from "react";
import { Device } from "../../entities";
import DeviceInfoTable from "../../components/device/deviceInfoTable";
import { getDevices } from "../../http/fetch";

export default function AllDevicesPage() {
  const [devices, setDevices] = useState<Device[]>([]);
  //Placeholder for now
  useEffect(() => {
    const get_devices = async () => {
      const data = await getDevices();
      if (data == undefined) {
        return;
      }
      setDevices(data);
    };
    get_devices();
  }, []);

  return (
    <section className="center">
      <DeviceInfoTable devices={devices} />
    </section>
  );
}
