import React from "react";
import { List } from "@mui/material";

import { NotificationEntity } from "../../entities";
import ErrorInfo from "../ui/errorInfo";
import { HttpError } from "../../http/fetch";
import NotificationFullListItem from "./notificationFullListItem";

type NotificationListProps = {
  notifications: NotificationEntity[];
};

const NotificationList: React.FC<NotificationListProps> = ({
  notifications,
}) => {
  if (!notifications) {
    return (
      <ErrorInfo
        title="No notifications found"
        error={new HttpError("No notifications", 404)}
      />
    );
  }
  return (
    <List>
      {notifications.map((notification) => (
        <NotificationFullListItem notification={notification} />
      ))}
    </List>
  );
};

export default NotificationList;
