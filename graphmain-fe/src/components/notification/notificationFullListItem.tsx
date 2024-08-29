import {
  Avatar,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Typography,
} from "@mui/material";
import { NotificationEntityType } from "../../entities";
import WarningIcon from "@mui/icons-material/Warning";
import InfoIcon from "@mui/icons-material/Info";
import ErrorIcon from "@mui/icons-material/Error";
import { Link } from "react-router-dom";
import { NotificationEntityQueryReturnType } from "../../http/fetch";
/**
 * A helper function to get consistent icons for Notifications
 * @param type Type of notification
 * @returns MUI icons-material icon JSX element
 */
const getIcon = (type: NotificationEntityType) => {
  switch (type) {
    case "warning":
      return <WarningIcon color="warning" />;
    case "info":
      return <InfoIcon color="primary" />;
    case "error":
      return <ErrorIcon color="error" />;
    default:
      return <InfoIcon />;
  }
};

export default function NotificationFullListItem({
  notification,
}: {
  notification: NotificationEntityQueryReturnType;
}) {
  return (
    <ListItem key={notification.id} alignItems="flex-start">
      <ListItemAvatar>
        <Avatar>{getIcon(notification.type)}</Avatar>
      </ListItemAvatar>
      <ListItemText
        primary={notification.message}
        secondary={
          <>
            <Typography component="span" variant="body2" color="text.primary">
              {new Date(notification.created_at).toLocaleString()}
            </Typography>
            <Link
              to={`/devices/${notification.device_id}/measurements/${notification.sensor_id}`}
            >
              {" — Measurement ID: " + notification.measurement.id + "\n"}
              {"Value: " + notification.measurement.value}
            </Link>
          </>
        }
      />
    </ListItem>
  );
}
