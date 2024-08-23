import { FC } from "react";
import { Sensor } from "../../entities";
import { Box, BoxProps } from "@mui/material";
interface IProps extends BoxProps {
  sensor: Sensor;
}
const MeasurementHead: FC<IProps> = function ({ sensor, ...props }) {
  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      padding="8px 16px"
      borderBottom="1px solid #ddd"
      bgcolor={"white"}
      {...props}
    >
      <Box flex="1" textAlign="left">
        Timestamp
      </Box>
      <Box flex="1" textAlign="center">
        Delta
      </Box>
      <Box flex="1" textAlign="right">
        Value ({sensor.unit})
      </Box>
    </Box>
  );
};
export default MeasurementHead;
