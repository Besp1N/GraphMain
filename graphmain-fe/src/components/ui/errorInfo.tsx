import { Alert, AlertTitle } from "@mui/material";

export default function ErrorInfo({
  title,
  error,
}: {
  title?: string;
  error: Error;
}) {
  return (
    <Alert severity="error">
      <AlertTitle>{title ?? "Something went wrong"}</AlertTitle>
      <p>{error.message}</p>
    </Alert>
  );
}
