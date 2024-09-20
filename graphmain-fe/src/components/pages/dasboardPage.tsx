import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../store/authStore";
import { useContext, useEffect } from "react";
import { useBreadcrumbs } from "../../store/appStore";
import Notifications from "../notification/notifications";
import { Box } from "@mui/material";
import { LastPeriodMeasurementGraph } from "../measurement/measurementsGraph";

export default function DashboardPage() {
  const navigate = useNavigate();
  const [, setBreadcrumbs] = useBreadcrumbs();

  useEffect(() => {
    setBreadcrumbs([]);
  }, []);
  const { loggedIn } = useContext(AuthContext);
  useEffect(() => {
    if (!loggedIn) navigate("/login");
  }, [loggedIn, navigate]);
  return (
    <div className="center">
      <h2>Recent notifications</h2>
      <Box mb={"5vh"}>
        <Notifications />
      </Box>
      <h2>Last week in the kettle</h2>

      <Box mb={"5vh"} height={"50vh"}>
        <LastPeriodMeasurementGraph period="week" sensorId={3} />
      </Box>
    </div>
  );
}
