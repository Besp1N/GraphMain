import { useEffect, useState } from "react";
import { Device } from "../../entities";

import { getDevice } from "../../http/fetch";

export default function DeviceDetailsPage({ id }: { id: Device["id"] }) {
  const [device, setDevice] = useState<Device>();
  //Placeholder for now
  useEffect(() => {
    const get_device = async () => {
      const data = await getDevice(id);
      if (data == undefined) {
        return;
      }
      setDevice(data);
    };
    get_device();
  }, [id]);
  return <div className="center">{device && device.id}</div>;
}
