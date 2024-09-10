import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom"; // Import to track the current route
import classes from "./mainNav.module.css";
import NavItem from "./navItem";

import { AuthContext } from "../../store/authStore";
import {
  Box,
  Button,
  Drawer,
  IconButton,
  List,
  ListItem,
  Paper,
  Theme,
  useMediaQuery,
} from "@mui/material";
import Breadcrumbs from "../ui/breadcrumbs";
import { AppContext } from "../../store/appStore";

import { Menu, NotificationsActive } from "@mui/icons-material"; // Import MUI icons
import { getToken, ROLE } from "../../http/authUtils";

export default function MainNav() {
  const { loggedIn, email, role } = useContext(AuthContext);
  const { messageQueue, connectWebSocket, stompClient } =
    useContext(AppContext)!;
  const location = useLocation();
  const [hasNewMessage, setHasNewMessage] = useState(false);
  const [isDrawerOpen, setDrawerOpen] = useState(false);
  const isSmallScreen = useMediaQuery((theme: Theme) =>
    theme.breakpoints.down("md")
  );

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

  const token = getToken();

  const renderNavItems = () => (
    <>
      <ListItem>
        <NavItem href={"/"}>
          {loggedIn ? "Dashboard" : "Login to continue"}
        </NavItem>
        {hasNewMessage && (
          <NotificationsActive
            color="error"
            className={classes["notification-icon"]}
          />
        )}
      </ListItem>
      {role === ROLE.ADMIN && (
        <ListItem>
          <NavItem href="/admin">Admin panel</NavItem>
        </ListItem>
      )}
      {loggedIn && (
        <>
          <ListItem>
            <NavItem href={"/devices"}>Devices</NavItem>
          </ListItem>
          <ListItem className={classes["user-group"]}>
            {!stompClient && (
              <Button
                onClick={() => connectWebSocket(token ?? "")}
                color="warning"
              >
                Reconnect
              </Button>
            )}
            <span>{email}</span>
            <div className="divider"></div>
            <NavItem href={"/logout"}>Logout</NavItem>
          </ListItem>
        </>
      )}
    </>
  );

  return (
    <Paper elevation={1} className={classes["main-nav"]}>
      {isSmallScreen ? (
        <Box display={"flex"} alignItems={"center"}>
          <IconButton
            onClick={() => setDrawerOpen(!isDrawerOpen)}
            style={{ float: "left" }}
          >
            <Menu />
          </IconButton>
          <Drawer
            anchor="left"
            open={isDrawerOpen}
            onClose={() => setDrawerOpen(false)}
          >
            <List>{renderNavItems()}</List>
          </Drawer>
        </Box>
      ) : (
        <ul>{renderNavItems()}</ul>
      )}
      <Breadcrumbs />
    </Paper>
  );
}
