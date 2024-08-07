
import { createTheme, ThemeProvider }  from '@mui/material';
import './App.css'
import { useEffect, useState } from 'react';
import { Device } from './entities';
import DeviceInfoTable from './components/device/deviceInfoTable';
import { getDevice } from './http/fetch';

function App() {
  const [devices, setDevices] = useState<Device[]>([]);
  //Placeholder for now
   useEffect( () => {
    const get_devices = async() => {
      const data = await getDevice(1);
      if (data == undefined) {
        return;
      }
      setDevices([data]);

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
