import { FC, useContext } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";
import FlashMessages from "../ui/flashMessages";
import { Button } from "@mui/material";
import { AppContext } from "../../store/appStore";
import { getToken } from "../../http/authUtils";
const Root: FC = function () {
  const { connectWebSocket } = useContext(AppContext)!;
  const token = getToken();
  return (
    <>
      <Button onClick={() => connectWebSocket(token ?? "")}>CONNECT WS</Button>
      <FlashMessages />
      <MainNav />
      <h1>GraphMain Development App</h1>
      <Outlet />
    </>
  );
};
export default Root;
