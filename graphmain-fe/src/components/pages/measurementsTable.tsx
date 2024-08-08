import { useParams } from "react-router-dom";
import { getMeasurements } from "../../http/fetch";
import { useFetchSafe } from "../../http/hooks";
import MeasurementTable from "../measurement/measurementTable";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { useCallback } from "react";
const MeasurementsTablePage = function () {
  const { sensorId: id } = useParams();
  //@ts-expect-error safeFetch will handle the NaN for now
  const cb = useCallback(() => getMeasurements(+id), [id]);

  const { loading, error, data: measurements } = useFetchSafe(cb);
  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorInfo error={error} />;
  }
  if (measurements == null) {
    return <p>Should be 404</p>;
  }
  return (
    <MeasurementTable
      measurements={measurements.sensor.measurementList ?? []}
      title="All recent measurements"
    />
  );
};
export default MeasurementsTablePage;
