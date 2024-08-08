import { FC, PropsWithChildren } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";

const Root: FC<PropsWithChildren> = function () {
  return (
    <>
      <MainNav />
      <h1>GraphMain Development App</h1>
      <Outlet />
    </>
  );
};
export default Root;
