import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { AuthContext } from "../../store//authStore";

const LogoutPage = () => {
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);
  useEffect(() => {
    logout();
    navigate("/");
  });
  return <></>;
};

export default LogoutPage;
