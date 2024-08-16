import { useFlash } from "../../store/flashStore";
import { Alert, AlertTitle, Snackbar } from "@mui/material";

const FlashMessages = () => {
  const { flashes, removeFlash } = useFlash();

  return (
    <>
      {flashes.map((flash) => (
        <Snackbar
          key={flash.id}
          open={true}
          autoHideDuration={6000}
          onClose={() => removeFlash(flash.id)}
        >
          <Alert
            onClose={() => removeFlash(flash.id)}
            severity={flash.type}
            sx={{ width: "100%" }}
          >
            <AlertTitle>
              {flash.type.charAt(0).toUpperCase() + flash.type.slice(1)}
            </AlertTitle>
            {flash.message}
          </Alert>
        </Snackbar>
      ))}
    </>
  );
};

export default FlashMessages;
