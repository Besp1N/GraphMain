import { FC } from "react";
import { getDevices } from "../../http/fetch";
import { useFetchSafe } from "../../http/hooks";
import DeviceInfoTable from "../device/deviceInfoTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { Device } from "../../entities";
const AllDevicesPage: FC = function () {
  const { loading, error, data: devices } = useFetchSafe<Device[]>(getDevices);
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
