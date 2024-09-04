import { Info } from '@mui/icons-material';
import { Button, TextField, Box, Typography } from '@mui/material';

function RegisterUserPage() {

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Register User
      </Typography>
      <TextField label="Email" fullWidth margin="normal" />
      <TextField label="Password" type="password" fullWidth margin="normal" />
      <Button variant="contained" color="primary" sx={{ mt: 2 }}>
        Register
      </Button>
      <Box display={"flex"} mt="1rem">
        <Info />
    <Typography>
        An e-mail will be sent to verify if the account has been created.
        If the user didn't receive it, you can retry account creation.
        There will be no issues with incorrect account creation. 
    </Typography>
      </Box>
    </Box>
  );
}

export default RegisterUserPage;
