import { FC, memo } from "react";
import {
  Breadcrumbs as MUIBreadcrumbs,
  Link,
  Typography,
  Box,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom"; // If you're using react-router for navigation
import { useBreadcrumbs } from "../../store/appStore";

const Breadcrumbs: FC = memo(function () {
  const [breadcrumbs] = useBreadcrumbs();

  return (
    <Box ml={"1rem"}>
      <MUIBreadcrumbs aria-label="breadcrumb">
        {breadcrumbs.map((breadcrumb, index) => {
          const isLast = index === breadcrumbs.length - 1;
          return isLast ? (
            <Typography color="textPrimary" key={breadcrumb[0]}>
              {breadcrumb[0]}
            </Typography>
          ) : (
            <Link
              key={breadcrumb[0]}
              color="inherit"
              component={RouterLink}
              to={breadcrumb[1]}
            >
              {breadcrumb[0]}
            </Link>
          );
        })}
      </MUIBreadcrumbs>
    </Box>
  );
});

export default Breadcrumbs;
