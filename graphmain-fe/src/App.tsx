
import { createTheme, ThemeProvider }  from '@mui/material';
import './App.css'
import { useEffect, useState } from 'react';
import { Device } from './entities';
import DeviceInfoTable from './components/device/deviceInfoTable';

function App() {
  const [devices, setDevices] = useState<Device[]>([]);
  //Placeholder for now
   useEffect( () => {
    const get_devices = async() => {
      const res = await fetch("http://127.0.0.1:8080/api/device/");
      if (!res.ok) {
        throw new Error("Cannot connect to host. :) ")
      }
      const data: Device[] = await res.json();
      console.log(data);
      setDevices(data);
    };
    get_devices();
  }, []);
  const theme = createTheme();
  return (
    <ThemeProvider theme={theme}>
      <h1>GraphMain Development App</h1>
      <div className='center'>
        <DeviceInfoTable devices={devices} />
      </div>
      <p className="read-the-docs">
        Hello world!
      </p>
      </ThemeProvider>
    )
}

export default App
