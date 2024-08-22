import { useContext } from "react";
import classes from "./mainNav.module.css";
import NavItem from "./navItem";
import { AuthContext } from "../../store/authStore";
export default function MainNav() {
  const { loggedIn, email } = useContext(AuthContext);
  return (
    <nav className={classes["main-nav"]}>
      <ul>
        <li>
          <NavItem href={"/"}>
            {loggedIn ? "Dashboard" : "Login to continue"}
          </NavItem>
        </li>

        {loggedIn ? (
          <>
            <li className="divider" />

            <li>
              <NavItem href={"/devices"}>Devices</NavItem>
            </li>

            <li className={classes["user-group"]}>
              <span>{email}</span>
              <div className="divider"></div>
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
