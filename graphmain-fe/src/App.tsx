import { createTheme, ThemeProvider } from "@mui/material";
import "./App.css";

import AllDevicesPage from "./components/pages/allDevices";
import DeviceDetailsPage from "./components/pages/deviceDetails";

function App() {
  const theme = createTheme();
  return (
    <ThemeProvider theme={theme}>
      <h1>GraphMain Development App</h1>
      <AllDevicesPage />
      <DeviceDetailsPage id={1} />
    </ThemeProvider>
  );
}

export default App;
