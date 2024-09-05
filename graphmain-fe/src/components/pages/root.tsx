import { FC, useContext, useEffect } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";
import FlashMessages from "../ui/flashMessages";
import { AppContext } from "../../store/appStore";
import { getToken } from "../../http/authUtils";
import { AuthContext } from "../../store/authStore";
const Root: FC = function () {
  const { connectWebSocket, stompClient } = useContext(AppContext)!;
  const { loggedIn } = useContext(AuthContext);
  // Try to establish a connection when user
  useEffect(() => {
    if (loggedIn && !stompClient) {
      const token = getToken();
      connectWebSocket(token ?? "");
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return (
    <>
     
      <FlashMessages />
      <MainNav />
      <h1>GraphMain Development App (Temporary Name)</h1>
      <Outlet />
    </>
  );
};
export default Root;
