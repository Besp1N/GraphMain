import { FC, PropsWithChildren, useContext } from "react";
import { Outlet } from "react-router-dom";
import MainNav from "../nav/mainNav";
import { AuthContext } from "../../store/authStore";
import FlashMessages from "../ui/flashMessages";

const Root: FC<PropsWithChildren> = function () {
  const { role } = useContext(AuthContext);
  return (
    <>
      <FlashMessages />
      <h3>Role: {role ?? "signed out"}</h3>
      <MainNav />
      <h1>GraphMain Development App</h1>
      <Outlet />
    </>
  );
};
export default Root;
