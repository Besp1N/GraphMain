import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom"; // Import to track the current route
import classes from "./mainNav.module.css";
import NavItem from "./navItem";
import { AuthContext } from "../../store/authStore";
import { Paper } from "@mui/material";
import Breadcrumbs from "../ui/breadcrumbs";
import { AppContext } from "../../store/appStore";

export default function MainNav() {
  const { loggedIn, email } = useContext(AuthContext);
  const { messageQueue } = useContext(AppContext)!;
  const location = useLocation(); // Get the current route
  const [hasNewMessage, setHasNewMessage] = useState(false);

  useEffect(() => {
    if (location.pathname !== "/notifications" && messageQueue.length > 0) {
      setHasNewMessage(true);
    }
  }, [messageQueue, location.pathname]);

  return (
    <Paper elevation={1} className={classes["main-nav"]}>
      <ul>
        <li>
          <NavItem href={"/"}>
            {loggedIn ? "Dashboard" : "Login to continue"}
          </NavItem>
        </li>

        {loggedIn ? (
          <>
            <li>
              <NavItem href={"/devices"}>Devices</NavItem>
            </li>
            <li>
              <NavItem
                href={"/notifications"}
                className={
                  hasNewMessage ? classes["notification-indicator"] : ""
                }
                onClick={() => setHasNewMessage(false)} // Reset on click
              >
                Notifications
              </NavItem>
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
      <Breadcrumbs />
    </Paper>
  );
}
