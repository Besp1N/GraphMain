import { useContext } from "react";
import classes from "./mainNav.module.css";
import NavItem from "./navItem";
import { AuthContext } from "../../store/authStore";
export default function MainNav() {
  const { loggedIn } = useContext(AuthContext);
  return (
    <nav className={classes["main-nav"]}>
      <ul>
        <li>
          <NavItem href={"/"}>
            {loggedIn ? "Dashboard" : "Login to continue"}
          </NavItem>
        </li>
        <li className="divider" />

        {loggedIn ? (
          <>
            <li>
              <NavItem href={"/devices"}>Devices</NavItem>
            </li>

            <li>
              <NavItem href={"/logout"}>Logout</NavItem>
            </li>
          </>
        ) : (
          ""
        )}
      </ul>
    </nav>
  );
}
