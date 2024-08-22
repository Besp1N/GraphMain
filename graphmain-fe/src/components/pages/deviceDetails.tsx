import { useCallback, useEffect } from "react";

import { getDevice } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import DeviceDetails from "../device/deviceDetails";
import { useParams } from "react-router-dom";
import Breadcrumbs from "../ui/breadcrumbs";

export default function DeviceDetailsPage() {
  useProtectedResource();

  const { deviceId: id } = useParams();
  //@ts-expect-error safeFetch will handle the NaN for now
  const cb = useCallback(() => getDevice(+id), [id]);

  const { loading, error, data: device, fetch } = useFetchSafe(cb);
  useEffect(() => fetch(), [fetch]);

  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorInfo error={error} />;
  }
  if (device == null) {
    return <p>Should be 404</p>;
  }
  return (
    <div className="center">
      <Breadcrumbs
        breadcrumbs={[
          ["Devices", `/devices`],
          [`Device ${id} : ${device.deviceName}`, ""],
        ]}
      />
      <DeviceDetails device={device} />
    </div>
  );
}
