import { FC } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";
import FlashMessages from "../ui/flashMessages";
const Root: FC = function () {
  return (
    <>
      <FlashMessages />
      <MainNav />
      <h1>GraphMain Development App</h1>
      <Outlet />
    </>
  );
};
export default Root;
