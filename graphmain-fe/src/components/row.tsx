import * as MUI from '@mui/material';
interface TableRowProps<T> {
    data: Array<T>,
    timestamp: EpochTimeStamp,
}
// Change name, it collides with MUI
export default function TableRow<T>(props: TableRowProps<T>) {
    return (
        <MUI.TableRow>
            <MUI.TableCell>
                {new Date(props.timestamp).toLocaleString("pl-PL", {
                    dateStyle: "short",
                    timeStyle: "short",
                    
                })}
            </MUI.TableCell>
            {props.data.map( (item, idx) => {
               return (<MUI.TableCell key={`${props.timestamp};${idx}`}>{""+item}</MUI.TableCell>);
            }) }
        </MUI.TableRow>
    );
}