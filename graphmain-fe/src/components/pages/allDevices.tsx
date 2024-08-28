import { FC, useEffect } from "react";
import { getDevices, HttpError } from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import DeviceInfoTable from "../device/deviceInfoTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { Device } from "../../entities";
import { useBreadcrumbs } from "../../store/appStore";
const AllDevicesPage: FC = function () {
  useProtectedResource();
  const [, setBreadcrumbs] = useBreadcrumbs();
  useEffect(() => {
    setBreadcrumbs([]);
  });
  const {
    loading,
    error,
    data: devices,
    fetch,
  } = useFetchSafe<Device[], HttpError>(getDevices);
  useEffect(() => fetch(), [fetch]);
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
