import {
  Container,
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
} from "@mui/material";
import { Device } from "../../entities";
import { Link } from "react-router-dom";
/**
 * React Component responsible for displaying details about a device
 * TODO: SPLIT IT INTO SMALLER COMPONENTS
 */
export default function DeviceDetails({ device }: { device: Device }) {
  return (
    <Container maxWidth="md" style={{ marginTop: "20px" }}>
      <Card>
        <CardContent>
          {/* Device Information */}
          <Typography variant="h5" component="div" gutterBottom>
            Device Details
          </Typography>
          <Typography variant="body1">
            <strong>Device Name:</strong> {device.deviceName}
          </Typography>
          <Typography variant="body1">
            <strong>Device Type:</strong> {device.deviceType}
          </Typography>

          {/* Sensors Information */}
          <Typography
            variant="h6"
            component="div"
            style={{ marginTop: "20px" }}
          >
            Sensors
          </Typography>
          {device.sensors && device.sensors.length > 0 ? (
            <List>
              {device.sensors.map((sensor) => (
                <ListItem key={sensor.id}>
                  <ListItemText
                    primary={sensor.sensorName}
                    secondary={`Type: ${sensor.sensorType}`}
                  />
                  <Link to={`/devices/${device.id}/measurements/${sensor.id}`}>
                    <Button variant="contained" color="primary" size="small">
                      View Measurements
                    </Button>
                  </Link>
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography variant="body2" color="textSecondary">
              No sensors available for this device.
            </Typography>
          )}
        </CardContent>
      </Card>
    </Container>
  );
}
