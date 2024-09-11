import { useState } from "react";
import { Box, Button, TextField, Typography, MenuItem } from "@mui/material";
import { ROLE } from "../../../http/authUtils";
import { registerUser, RegistrationRequest } from "../../../http/fetch";
import { Info } from "@mui/icons-material";
import ErrorInfo from "../../ui/errorInfo";
import { useFlash } from "../../../store/flashStore";
import Spinner from "../../ui/spinner";
const EMPTY_FORM_DATA: RegistrationRequest = {
  email: "",
  password: "",
  role: ROLE.USER, // Default role
  name: "",
  lastName: "",
};
function RegisterUserPage() {
  const { addFlash } = useFlash();
  const [error, setError] = useState<Error | undefined>();
  const [loading, setLoading] = useState<boolean>(false);

  const [formData, setFormData] =
    useState<RegistrationRequest>(EMPTY_FORM_DATA);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData: RegistrationRequest) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    setError(undefined);
    setLoading(true);
    const result = await registerUser(formData);
    if (result instanceof Error) {
      setError(result);
    } else {
      addFlash(
        "success",
        `Authenticated user ${
          result!.email
        }. Please check email to see if the registration is correct.`
      );
      setFormData(EMPTY_FORM_DATA);
    }
    setLoading(false);
  };

  return (
    <Box>
      {error ? <ErrorInfo error={error} /> : ""}
      <Typography variant="h4" gutterBottom>
        Register User
      </Typography>
      <TextField
        label="Email"
        name="email"
        fullWidth
        margin="normal"
        value={formData.email}
        onChange={handleChange}
      />
      <TextField
        label="Password"
        name="password"
        type="password"
        fullWidth
        margin="normal"
        value={formData.password}
        onChange={handleChange}
      />
      <TextField
        label="First Name"
        name="name"
        fullWidth
        margin="normal"
        value={formData.name}
        onChange={handleChange}
      />
      <TextField
        label="Last Name"
        name="lastName"
        fullWidth
        margin="normal"
        value={formData.lastName}
        onChange={handleChange}
      />
      <TextField
        label="Role"
        name="role"
        select
        fullWidth
        margin="normal"
        value={formData.role}
        onChange={handleChange}
      >
        <MenuItem value={ROLE.USER}>User</MenuItem>
        <MenuItem value={ROLE.ADMIN}>Admin</MenuItem>
      </TextField>
      {loading ? (
        <Spinner />
      ) : (
        <Button
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
          onClick={handleSubmit}
        >
          Register
        </Button>
      )}
      <Box display={"flex"} mt="1rem">
        <Info />
        <Typography>
          An e-mail will be sent to verify if the account has been created. If
          the user didn't receive it, you can retry account creation. There will
          be no issues with incorrect account creation.
        </Typography>
      </Box>
    </Box>
  );
}

export default RegisterUserPage;
