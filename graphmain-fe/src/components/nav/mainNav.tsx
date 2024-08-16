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
          <NavItem href={"/"}>Home</NavItem>
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
          <li>
            <NavItem href={"/login"}>Login</NavItem>
          </li>
        )}
      </ul>
    </nav>
  );
}
