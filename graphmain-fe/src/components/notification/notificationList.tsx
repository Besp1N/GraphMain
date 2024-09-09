import React from "react";
import { List } from "@mui/material";

import ErrorInfo from "../ui/errorInfo";
import { HttpError, NotificationEntityQueryReturnType } from "../../http/fetch";
import NotificationFullListItem from "./notificationFullListItem";

type NotificationListProps = {
  notifications: NotificationEntityQueryReturnType[];
};

const NotificationList: React.FC<NotificationListProps> = ({
  notifications,
}) => {
  if (!notifications || !notifications.length) {
    return (
      <ErrorInfo
        title="No notifications found"
        error={new HttpError("We couldn't find any new notifications.", 404)}
      />
    );
  }
  return (
    <List>
      {notifications.map((notification) => (
        <NotificationFullListItem
          notification={notification}
          key={notification.id}
        />
      ))}
    </List>
  );
};

export default NotificationList;
