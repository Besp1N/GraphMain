import { FC, useCallback, useContext, useEffect, useState } from "react";
import {
  getLatestNotifications,
  HttpError,
  NotificationEntityQueryReturnType,
} from "../../http/fetch";
import { useFetchSafe, useProtectedResource } from "../../http/hooks";
import ErrorInfo from "../ui/errorInfo";
import Spinner from "../ui/spinner";
import { AppContext, useBreadcrumbs } from "../../store/appStore";
import NotificationList from "../notification/notificationList";
import { Pagination, Paper } from "@mui/material";
/**
 * Component managing fetching and displaying notifications
 */
const Notifications: FC = function () {
  useProtectedResource();
  const { setMessageQueue } = useContext(AppContext)!;
  // clear message queue on the notifications render
  useEffect(() => {
    setMessageQueue([]);
  }, []);
  const [, setBreadcrumbs] = useBreadcrumbs();
  useEffect(() => {
    setBreadcrumbs([]);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const [page, setPage] = useState(1); // Start with page 1

  // "page - 1" because MUI indexes from 1 because no one indexes from 0 apparently
  const fetcher = useCallback(() => getLatestNotifications(page - 1), [page]);
  const {
    loading,
    error,
    data: notifications,
    fetch,
  } = useFetchSafe<NotificationEntityQueryReturnType[], HttpError>(fetcher);

  useEffect(() => {
    fetch();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [fetch]);
  const handlePageChange = (
    _event: React.ChangeEvent<unknown>,
    value: number
  ) => {
    setPage(value);
  };

  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorInfo error={error} />;
  }

  return (
    <Paper>
      <NotificationList notifications={notifications ?? []} />
      <Pagination
        count={notifications?.at(0)?.totalPages ?? 0}
        page={page}
        onChange={handlePageChange}
        color="primary"
        sx={{ mt: 2, display: "flex", justifyContent: "center" }}
      />
    </Paper>
  );
};

export default Notifications;
