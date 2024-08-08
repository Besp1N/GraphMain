import { createTheme, ThemeProvider } from "@mui/material";
import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { FC } from "react";
import DeviceDetailsPage from "./components/pages/deviceDetails";
import AllDevicesPage from "./components/pages/allDevices";
import Root from "./components/pages/root";
import MeasurementsTablePage from "./components/pages/measurementsTable";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "/devices",
        element: <AllDevicesPage />,
      },
      {
        path: "/devices/:deviceId",
        element: <DeviceDetailsPage />,
      },
      {
        path: "/devices/measurements/:sensorId",
        element: <MeasurementsTablePage />,
      },
    ],
  },
]);

const App: FC = function () {
  const theme = createTheme();
  return (
    <ThemeProvider theme={theme}>
      <RouterProvider router={router} />
    </ThemeProvider>
  );
};

export default App;
