import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom"; // Import to track the current route
import classes from "./mainNav.module.css";
import NavItem from "./navItem";
import { AuthContext } from "../../store/authStore";
import { Button, Paper } from "@mui/material";
import Breadcrumbs from "../ui/breadcrumbs";
import { AppContext } from "../../store/appStore";

import { NotificationsActive } from "@mui/icons-material"; // Import MUI icons
import { getToken, ROLE } from "../../http/authUtils";

export default function MainNav() {
  const { loggedIn, email, role } = useContext(AuthContext);
  const { messageQueue } = useContext(AppContext)!;
  const location = useLocation(); // Get the current route
  const [hasNewMessage, setHasNewMessage] = useState(false);

  useEffect(() => {
    if (messageQueue.length > 0) {
      console.log("new notif");
      if (location.pathname !== "/") {
        alert(
          `New notification arrived.\n${
            messageQueue[messageQueue.length - 1].message
          }\nPlease refresh the page to see more.`
        );
      }
      setHasNewMessage(true);
    }
  }, [messageQueue, location.pathname]);
  const { connectWebSocket, stompClient } = useContext(AppContext)!;
  const token = getToken();

  return (
    <Paper elevation={1} className={classes["main-nav"]}>
      <ul>
        <li>
          <NavItem href={"/"}>
            {loggedIn ? "Dashboard" : "Login to continue"}
          </NavItem>
          {hasNewMessage ? (
            <NotificationsActive
              color="error"
              className={classes["notification-icon"]}
            />
          ) : (
            ""
          )}
        </li>
        <li>
          {role == ROLE.ADMIN ? (
            <NavItem href="/admin">Admin panel</NavItem>
          ) : (
            ""
          )}
        </li>

        {loggedIn ? (
          <>
            <li>
              <NavItem href={"/devices"}>Devices</NavItem>
            </li>
            <li></li>

            <li className={classes["user-group"]}>
              {!stompClient ? (
                <Button
                  onClick={() => connectWebSocket(token ?? "")}
                  color="warning"
                >
                  Reconnect
                </Button>
              ) : (
                ""
              )}
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
