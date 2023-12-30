import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import React from 'react';
import { FileUpload } from './FileUpload';



export interface CreateLinkDialogProps {
  open: boolean
  onClose?: () => void
  onCreateLink?: (name: string, form: FormData) => Promise<void>
}

export const CreateLinkDialog = (props: CreateLinkDialogProps) => {
  const [name, setName] = React.useState<string | undefined>(undefined)
  const [iconFile, setIconFile] = React.useState<string | undefined>(undefined)

  React.useEffect(() => {
    setName(undefined)
    setIconFile(undefined)
  }, [props.open])


  const handleCreateLink = (e: React.FormEvent<HTMLFormElement>) => {
    const formData = new FormData(e.target as HTMLFormElement);

    if(props.onCreateLink) {
      props.onCreateLink(name!, formData).then(()=>{if(props.onClose) props.onClose()})
    } else if(props.onClose) {
      props.onClose()
    }

    e.preventDefault()
  }

  return <Dialog
    open = {props.open}
    onClose={props.onClose}
  >
    <DialogTitle>Create Link</DialogTitle>
    <IconButton
          onClick={props.onClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
    </IconButton>
    <DialogContent>
      <Box
        id="create-link-form"
        component="form"
        sx={{
          width: "400px",
          '& .MuiTextField-root': { m: 1 },
        }}
        //noValidate
        //autoComplete="off"
        onSubmit={handleCreateLink}
      >
        <div>
          <TextField sx={{ width: '100%' }} size="small" label="Name" onChange={e => setName(e.target.value)}/>
        </div>
        <div>
          <TextField name="href" sx={{ width: '100%' }} size="small" label="Href"/>
        </div>
        <div style={{
          display: "flex",
          alignItems: "center"
        }}>
          <FileUpload name="icon" value={iconFile} onChange={e => setIconFile(e.target.value)}/>
        </div>
      </Box>
    </DialogContent>
    <DialogActions>
      <Button type='submit' form='create-link-form'>Create</Button>
    </DialogActions>
  </Dialog>
}