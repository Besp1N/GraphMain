import { Box, Typography } from '@mui/material';
import {  getUsers } from '../../../http/fetch';
import { useFetchSafe } from '../../../http/hooks';
import Spinner from '../../ui/spinner';
import ErrorInfo from '../../ui/errorInfo';
import UserList from '../../user/userList';
import { User } from '../../../entities';
import { useEffect } from 'react';

const onDelete = function(id: User["id"]) {
    return ;
}
function AllUsersPage() {
    
   const {loading, data: users, error, fetch} = useFetchSafe(getUsers);
   useEffect(() => fetch(), []);
  if (error) {
    return <ErrorInfo error={error} />;
  }
  
  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        All Users
      </Typography>
      {loading ? <Spinner /> : <UserList onDelete={onDelete} users={users ?? []} /> }

      
    </Box>
  );
}

export default AllUsersPage;
