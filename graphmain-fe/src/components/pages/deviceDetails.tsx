import { useCallback, useEffect } from "react";

import { getDevice } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import DeviceDetails from "../device/deviceDetails";
import { useParams } from "react-router-dom";
import { useBreadcrumbs } from "../../store/appStore";

export default function DeviceDetailsPage() {
  useProtectedResource();

  const { deviceId: id } = useParams();
  //@ts-expect-error safeFetch will handle the NaN for now
  const cb = useCallback(() => getDevice(+id), [id]);
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_, setBreadcrumbs] = useBreadcrumbs();

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
  setBreadcrumbs([
    ["Devices", `/devices`],
    [`Device ${id} : ${device.deviceName}`, ""],
  ]);
  return (
    <div className="center">
      <DeviceDetails device={device} />
    </div>
  );
}
