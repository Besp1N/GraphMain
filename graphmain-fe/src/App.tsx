import { createTheme, ThemeProvider } from "@mui/material";
import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { FC } from "react";
import DeviceDetailsPage from "./components/pages/deviceDetails";
import AllDevicesPage from "./components/pages/allDevices";
import Root from "./components/pages/root";
import MeasurementsTablePage from "./components/pages/measurementsTablePage.tsx";
import { AuthProvider } from "./store/authStore.tsx";
import LoginPage from "./components/pages/loginPage.tsx";
import LogoutPage from "./components/pages/logoutPage.tsx";
import { FlashProvider } from "./store/flashStore.tsx";
import ErrorPage from "./components/pages/errorPage.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/login",
        element: <LoginPage />,
      },
      {
        path: "/logout",
        element: <LogoutPage />,
      },
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
    <AuthProvider>
      <ThemeProvider theme={theme}>
        <FlashProvider>
          <RouterProvider router={router} />
        </FlashProvider>
      </ThemeProvider>
    </AuthProvider>
  );
};

export default App;
