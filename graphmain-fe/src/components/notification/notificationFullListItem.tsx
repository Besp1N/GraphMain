import {
  Avatar,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Typography,
} from "@mui/material";
import { NotificationEntity, NotificationEntityType } from "../../entities";
import WarningIcon from "@mui/icons-material/Warning";
import InfoIcon from "@mui/icons-material/Info";
import ErrorIcon from "@mui/icons-material/Error";
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
    case "critical":
      return <ErrorIcon color="error" />;
    default:
      return <InfoIcon />;
  }
};

export default function NotificationFullListItem({
  notification,
}: {
  notification: NotificationEntity;
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
            {" — Measurement ID: " + notification.measurement_id}
          </>
        }
      />
    </ListItem>
  );
}
