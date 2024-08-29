import { FC, useContext, useEffect } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";
import FlashMessages from "../ui/flashMessages";
import { Button } from "@mui/material";
import { AppContext } from "../../store/appStore";
import { getToken } from "../../http/authUtils";
import { AuthContext } from "../../store/authStore";
const Root: FC = function () {
  const { connectWebSocket, stompClient } = useContext(AppContext)!;
  const { loggedIn } = useContext(AuthContext);
  const token = getToken();
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
      {stompClient ? (
        <p>Connected via WebSocket</p>
      ) : (
        <Button onClick={() => connectWebSocket(token ?? "")}>
          CONNECT WS
        </Button>
      )}
      <FlashMessages />
      <MainNav />
      <h1>GraphMain Development App</h1>
      <Outlet />
    </>
  );
};
export default Root;
