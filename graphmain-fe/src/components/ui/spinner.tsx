import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';

export default function Spinner() {
  return (
      <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
      >
        <CircularProgress />
      </Box>
  );
}
