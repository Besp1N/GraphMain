import { FC } from "react";
import { Breadcrumbs as MUIBreadcrumbs, Link, Typography } from "@mui/material";
import { Link as RouterLink } from "react-router-dom"; // If you're using react-router for navigation

interface IBreadCrumbsProps {
  breadcrumbs: [string, string][];
}

const Breadcrumbs: FC<IBreadCrumbsProps> = function ({ breadcrumbs }) {
  return (
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
  );
};

export default Breadcrumbs;
