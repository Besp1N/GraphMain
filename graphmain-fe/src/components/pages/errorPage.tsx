import { useNavigate } from "react-router-dom";
import { Container, Typography, Button, Box } from "@mui/material";

const ErrorPage = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <Container sx={{ textAlign: "center", marginTop: 8 }}>
      <Typography variant="h1" component="h1" gutterBottom>
        Oops!
      </Typography>
      <Typography variant="h5" component="h2" gutterBottom>
        Something went wrong.
      </Typography>
      <Typography variant="body1" sx={{ marginBottom: 4 }}>
        The page you are looking for might have been removed, had its name
        changed, or is temporarily unavailable.
      </Typography>
      <Box>
        <Button variant="contained" color="primary" onClick={handleGoHome}>
          Go to Homepage
        </Button>
      </Box>
    </Container>
  );
};

export default ErrorPage;
