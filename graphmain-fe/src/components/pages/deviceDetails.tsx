import { useCallback } from "react";

import { getDevice } from "../../http/fetch";
import { useFetchSafe } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import DeviceDetails from "../device/deviceDetails";
import { useParams } from "react-router-dom";

export default function DeviceDetailsPage() {
  const { deviceId: id } = useParams();
  //@ts-expect-error safeFetch will handle the NaN for now
  const cb = useCallback(() => getDevice(+id), [id]);

  const { loading, error, data: device } = useFetchSafe(cb);
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
      <DeviceDetails device={device} />
    </div>
  );
}
