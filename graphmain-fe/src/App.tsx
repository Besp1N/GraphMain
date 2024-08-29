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
import DashboardPage from "./components/pages/dasboardPage.tsx";
import { AppContextProvider } from "./store/appStore.tsx";
import NotificationsPage from "./components/pages/notificationsPage.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/",
        element: <DashboardPage />,
      },
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
        path: "/notifications",
        element: <NotificationsPage />,
      },
      {
        path: "/devices/:deviceId",
        element: <DeviceDetailsPage />,
      },
      {
        path: "/devices/:deviceId/measurements/:sensorId",
        element: <MeasurementsTablePage />,
      },
    ],
  },
]);

const App: FC = function () {
  const theme = createTheme({ palette: { mode: "light" } });

  return (
    <AppContextProvider>
      <AuthProvider>
        <ThemeProvider theme={theme}>
          <FlashProvider>
            <RouterProvider router={router} />
          </FlashProvider>
        </ThemeProvider>
      </AuthProvider>
    </AppContextProvider>
  );
};

export default App;
