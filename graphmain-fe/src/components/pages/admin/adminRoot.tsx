import { AppBar, Toolbar, Typography, Tabs, Tab, Box } from '@mui/material';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { useProtectedResource } from '../../../http/hooks';
import { ROLE } from '../../../http/authUtils';
const TABS = ["/admin/register", "/admin/users"];
function AdminPanel() {
  useProtectedResource(ROLE.ADMIN);
  const location = useLocation();
  const tabIndex = TABS.indexOf(location.pathname);

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Admin Panel
          </Typography>
        </Toolbar>
      </AppBar>
      <Tabs value={tabIndex} centered>
        <Tab label="Register User" component={Link} to="register" />
        <Tab label="All Users" component={Link} to="users" />
      </Tabs>
      <Box sx={{ p: 3 }}>
        <Outlet />
      </Box>
    </Box>
  );
}

export default AdminPanel;
