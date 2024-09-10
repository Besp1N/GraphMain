import { Box, Typography } from "@mui/material";
import { deleteUser, getUsers } from "../../../http/fetch";
import { useFetchSafe } from "../../../http/hooks";
import Spinner from "../../ui/spinner";
import ErrorInfo from "../../ui/errorInfo";
import UserList from "../../user/userList";
import { User } from "../../../entities";
import { useEffect } from "react";

function AllUsersPage() {
  const { loading, data: users, error, fetch } = useFetchSafe(getUsers);

  const onDelete = async function (user: User) {
    const confirmed = window.confirm(
      `Are you sure you want to delete user '${user.email}'?`
    );
    if (confirmed) {
      await deleteUser(user.id);
      await fetch(); // Refetch the users after deleting
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  if (error) {
    return <ErrorInfo error={error} />;
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        All Users
      </Typography>
      {loading ? (
        <Spinner />
      ) : (
        <UserList onDelete={onDelete} users={users ?? []} />
      )}
    </Box>
  );
}

export default AllUsersPage;
