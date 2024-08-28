import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../store/authStore";
import { useContext, useEffect } from "react";
import { useBreadcrumbs } from "../../store/appStore";

export default function DashboardPage() {
  const navigate = useNavigate();
  const [, setBreadcrumbs] = useBreadcrumbs();
  useEffect(() => {
    setBreadcrumbs([]);
  });
  const { loggedIn } = useContext(AuthContext);
  useEffect(() => {
    if (!loggedIn) navigate("/login");
  }, [loggedIn, navigate]);
  return (
    <div className="center">
      <h2>DASHBOARD</h2>
    </div>
  );
}
