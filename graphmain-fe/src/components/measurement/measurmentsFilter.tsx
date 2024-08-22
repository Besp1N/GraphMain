import { Box, Button, Paper } from "@mui/material";
import type { Option } from "../../http/fetch";
import { memo, useState, FC, ChangeEvent } from "react";
export type MeasurementsFilters = {
  from: Option<number>;
  to: Option<number>;
};
export interface MeasurementFiltersProps {
  onFiltersChange: (filters: MeasurementsFilters) => void;
  initialValues: MeasurementsFilters;
}
const MeasurementsFilters: FC<MeasurementFiltersProps> = memo(
  function MeasurementsFilter({ initialValues, onFiltersChange }) {
    const [from, setFrom] = useState("");
    const [to, setTo] = useState("");

    const handleFromChange = (event: ChangeEvent<HTMLInputElement>) => {
      setFrom(event.target.value);
    };

    const handleToChange = (event: ChangeEvent<HTMLInputElement>) => {
      setTo(event.target.value);
    };

    return (
      <Box display="flex" justifyContent="space-around" mt={2}>
        <Paper>
          <input
            type="datetime-local"
            defaultValue={initialValues.from}
            value={from}
            onChange={handleFromChange}
          />
          <span> - </span>
          <input
            type="datetime-local"
            defaultValue={initialValues.to}
            value={to}
            onChange={handleToChange}
          />
          <Button
            onClick={() => {
              if (onFiltersChange) {
                onFiltersChange({
                  from: from ? new Date(from).getTime() / 1000 : undefined,
                  to: to ? new Date(to).getTime() / 1000 : undefined,
                });
              }
            }}
          >
            Filter results
          </Button>
        </Paper>
      </Box>
    );
  }
);

export default MeasurementsFilters;
