import * as React from 'react';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import TextField from '@mui/material/TextField';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { visuallyHidden } from '@mui/utils';
import { Link, LinkService } from '../services/LinksService';
import { CreateLinkDialog } from './CreateLinkDialog';
import { DeleteDialog } from './DeleteDialog';
import { request } from '../services/ServiceResponse';
import { nop } from '../utils/FunctionUtils';

interface Row {
  id: number,
  name: string,
  icon: string,
  href: string
}

interface EditColumn {
  rowId: number,
  column: keyof Row
}

function createRowArray(links: Link[]): Row[] {
  return links.map((link, i) => createRow(i, link))
}

function createRow(
  id: number,
  link: Link,
): Row {
  return {
    id,
    name: link.name,
    icon: link.icon!,
    href: link.href
  };
}

function descendingComparator<T>(a: T, b: T, orderBy: keyof T) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

type Order = 'asc' | 'desc';

function getComparator<Key extends keyof any>(
  order: Order,
  orderBy: Key,
): (
  a: { [key in Key]: number | string },
  b: { [key in Key]: number | string },
) => number {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

// Since 2020 all major browsers ensure sort stability with Array.prototype.sort().
// stableSort() brings sort stability to non-modern browsers (notably IE11). If you
// only support modern browsers you can replace stableSort(exampleArray, exampleComparator)
// with exampleArray.slice().sort(exampleComparator)
function stableSort<T>(array: readonly T[], comparator: (a: T, b: T) => number) {
  const stabilizedThis = array.map((el, index) => [el, index] as [T, number]);
  stabilizedThis.sort((a, b) => {
    const order = comparator(a[0], b[0]);
    if (order !== 0) {
      return order;
    }
    return a[1] - b[1];
  });
  return stabilizedThis.map((el) => el[0]);
}

interface EnhancedTableProps {
  onRequestSort: (event: React.MouseEvent<unknown>, property: keyof Row) => void;
  order: Order;
  orderBy: string;
  rowCount: number;
}

function EnhancedTableHead(props: EnhancedTableProps) {
  const { order, orderBy, rowCount, onRequestSort } =
    props;
  const createSortHandler =
    (property: keyof Row) => (event: React.MouseEvent<unknown>) => {
      onRequestSort(event, property);
    };

  return (
    <TableHead>
      <TableRow>
        <TableCell
          key='name'
          align={'left'}
          sortDirection={orderBy === 'name' ? order : false}
          style={{width: "20%"}}
        >
            <TableSortLabel
              active={orderBy === 'name'}
              direction={orderBy === 'name' ? order : 'asc'}
              onClick={createSortHandler('name')}
            >
              {'Name'}
              {orderBy === 'name' ? (
                <Box component="span" sx={visuallyHidden}>
                  {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                </Box>
              ) : null}
            </TableSortLabel>
        </TableCell>
        <TableCell
          key='icon'
          align='left'
          padding='none'
          style={{width: "10%"}}
        >
            Icon
        </TableCell>
        <TableCell
          key='href'
          align='left'
          style={{width: "60%"}}
        >
            Href
        </TableCell>
        <TableCell
          key='buttons'
          align='right'
          style={{width: "10%"}}
        >
        </TableCell>
      </TableRow>
    </TableHead>
  );
}

interface EnhancedTableToolbarProps {
  onCreateLink?: ()=>void
}

function EnhancedTableToolbar(props: EnhancedTableToolbarProps) {

  return (
    <Toolbar
      sx={{
        pl: { sm: 2 },
        pr: { xs: 1, sm: 1 },
      }}
    >
      <Typography
          sx={{ flex: '1 1 100%' }}
          variant="h6"
          id="tableTitle"
          component="div"
        >
          Links
      </Typography>
      <Tooltip title="Create New Link">
          <IconButton onClick={props.onCreateLink}>
            <AddIcon />
          </IconButton>
        </Tooltip>
    </Toolbar>
  );
}


export interface LinkTabProps {
  onLinksUpdated?: () => void,
  onError?: (msg: string) => void,
}


interface TextFieldWrapperProps {
  defaultValue?: string
  onBlur?: (e: React.FocusEvent<HTMLInputElement>) => void
}

export const TextFieldWrapper = (props: TextFieldWrapperProps) => {
  const inputRef = React.useRef<HTMLInputElement>();
  
  React.useEffect(() => {
    if(inputRef.current) {
      inputRef.current.select()
    }
  }, [inputRef])

  return <TextField
    inputRef={inputRef}
    hiddenLabel
    size="small"
    variant="filled"
    focused
    defaultValue={props.defaultValue}
    onBlur={props.onBlur}
  />
}

export const LinkTab = (props: LinkTabProps) => {
  const [links, setLinks] = React.useState<Link[]>([]);
  const [rows, setRows] = React.useState<Row[]>([]);
  const [order, setOrder] = React.useState<Order>('asc');
  const [orderBy, setOrderBy] = React.useState<keyof Row>('name');
  
  const [deleteDialog_open, setDeleteDialog_open] = React.useState(false);
  const [deleteDialog_message, setDeleteDialog_message] = React.useState("");
  const [deleteDialog_row, setDeleteDialog_row] = React.useState<Row | undefined>(undefined);

  const [createLinkDialog_open, setCreateLinkDialog_open] = React.useState(false);

  const load = () => request(LinkService.getLinks(), 
    links => {    
      setLinks(links!)
      setRows(createRowArray(links!))
    },
    msg => props.onError ? props.onError(msg) : nop())

  React.useEffect(() => {
    load()
  }, [])

  React.useEffect(() => {
    setRows(createRowArray(links))
  }, [links])

  const handleDeleteClick = (row: Row) => {
    setDeleteDialog_row(row)
    setDeleteDialog_message(`Are you sure you want to delete ${row.name}?`)
    setDeleteDialog_open(true)
  }

  const handleDeleteDialogClose = () => {
    setDeleteDialog_open(false)
    setDeleteDialog_row(undefined)
  }

  const handleDeleteLink =  async () => {
    if(!deleteDialog_row) {
      throw "delete row not defined"
    }

    await request(LinkService.deleteLink(deleteDialog_row?.name),
      undefined,
      msg => props.onError ? props.onError(msg) : nop())

    await load()
    props.onLinksUpdated ? props.onLinksUpdated() : nop()
  }

  const handleCreateLink = async (name: string, form: FormData) => {
    await request(LinkService.createLink(name, form),
      undefined,
      msg => props.onError ? props.onError(msg) : nop())
    await load()
    if(props.onLinksUpdated) props.onLinksUpdated()
  }

  const handleRequestSort = (
    event: React.MouseEvent<unknown>,
    property: keyof Row,
  ) => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };

  const visibleRows = React.useMemo(
    () => stableSort(rows, getComparator(order, orderBy)),
    [order, orderBy, rows],
  );


  return (
    <Box sx={{ width: '100%' }}>
      <DeleteDialog
        open = {deleteDialog_open}
        message = {deleteDialog_message}
        onClose = {handleDeleteDialogClose}
        doDelete={handleDeleteLink}
      />
      <CreateLinkDialog 
        open = {createLinkDialog_open}
        onClose={() => setCreateLinkDialog_open(false) }
        onCreateLink={handleCreateLink}
      />
      <Paper sx={{ width: '100%', mb: 2 }}>
        <EnhancedTableToolbar
          onCreateLink={() => setCreateLinkDialog_open(true)}
        />
        <TableContainer>
          <Table
            aria-labelledby="tableTitle"
            size="medium"
          >
            <EnhancedTableHead
              order={order}
              orderBy={orderBy}
              onRequestSort={handleRequestSort}
              rowCount={rows.length}
            />
            <TableBody>
              {visibleRows.map((row, index) => {
                const labelId = `enhanced-table-checkbox-${index}`;

                return (
                  <TableRow
                    hover
                    role="checkbox"
                    tabIndex={-1}
                    key={row.id}
                  >
                    <TableCell
                      component="th"
                      id={labelId}
                      scope="row"
                    >
                      {row.name}
                    </TableCell>
                    <TableCell 
                      sx={{ padding: 0 }}
                    >
                      <img src={row.icon} style={{ height: "32px", width: "32px" }} />
                    </TableCell>
                    <TableCell>{row.href}</TableCell>
                    <TableCell 
                      sx={{ padding: 0 }}
                    >
                      <Tooltip title="Delete" enterDelay={500} placement="right">
                        <IconButton onClick={() => {handleDeleteClick(row)} }>
                          <DeleteIcon />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                );
              })}
              
            </TableBody>
          </Table>
        </TableContainer>
        
      </Paper>
    </Box>
  );
}