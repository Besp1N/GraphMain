
import { createTheme, TableBody, TableCell, TableHead, ThemeProvider }  from '@mui/material';
import * as MUI from '@mui/material';
import './App.css'
import TableRow from './components/row';

function App() {
  
  const data = [1, 2, 3, 4, 12];
  const theme = createTheme();
  return (
    <ThemeProvider theme={theme}>
      <h1>Vite + React</h1>
      <MUI.Table>
        <TableHead>
          <MUI.TableRow>
              <TableCell>
                Timestamp
              </TableCell>
            </MUI.TableRow>
          </TableHead>
        <TableBody>
          <TableRow data={data} timestamp={1221} />
        </TableBody>
      </MUI.Table>
      <p className="read-the-docs">
        Hello world!
      </p>
      </ThemeProvider>
    )
}

export default App
