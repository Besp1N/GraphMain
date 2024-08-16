import { FC } from "react";
import { getDevices, HttpError } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import DeviceInfoTable from "../device/deviceInfoTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { Device } from "../../entities";
const AllDevicesPage: FC = function () {
  useProtectedResource();
  const {
    loading,
    error,
    data: devices,
  } = useFetchSafe<Device[], HttpError>(getDevices);
  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorInfo error={error} />;
  }

  return (
    <section className="center">
      <DeviceInfoTable devices={devices ?? []} />
    </section>
  );
};

export default AllDevicesPage;
