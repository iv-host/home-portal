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
import { Link } from '../services/LinksService';



export interface CreateLinkDialogProps {
  open: boolean
  onClose?: () => void
  onCreateLink?: (name: string, form: FormData) => Promise<void>
  link?: Link
  onUpdateLink?: (oldName: string, form: FormData) => Promise<void>
}

export const CreateLinkDialog = (props: CreateLinkDialogProps) => {
  const [name, setName] = React.useState('')
  const [href, setHref] = React.useState('')
  const [roles, setRoles] = React.useState('')
  const [iconFile, setIconFile] = React.useState<string | undefined>(undefined)

  React.useEffect(() => {
    if (props.open) {
      setName(props.link?.name ?? '')
      setHref(props.link?.href ?? '')
      setRoles(props.link?.roles.join(', ') ?? '')
    }
    setIconFile(undefined)
  }, [props.open, props.link])


  const handleCreateLink = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const linkRoles = roles.split(',').map(role => role.trim()).filter(Boolean)
    formData.set('name', name.trim())
    formData.set('href', href.trim())
    formData.delete('roles')
    linkRoles.forEach(role => formData.append('roles', role))

    const save = props.link
      ? props.onUpdateLink?.(props.link.name, formData)
      : props.onCreateLink?.(name.trim(), formData)
    if(save) {
      save.then(() => props.onClose?.())
    } else if(props.onClose) {
      props.onClose()
    }
  }

  return <Dialog
    open = {props.open}
    onClose={props.onClose}
  >
    <DialogTitle>{props.link ? 'Edit Link' : 'Create Link'}</DialogTitle>
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
          <TextField name="name" required sx={{ width: '100%' }} size="small" label="Name" value={name} onChange={e => setName(e.target.value)}/>
        </div>
        <div>
          <TextField name="href" required sx={{ width: '100%' }} size="small" label="Href" value={href} onChange={e => setHref(e.target.value)}/>
        </div>
        <div>
          <TextField name="roles" sx={{ width: '100%' }} size="small" label="Roles" helperText="Comma-separated; leave blank to show to everyone" value={roles} onChange={e => setRoles(e.target.value)}/>
        </div>
        {props.link && <div style={{ padding: '0 8px' }}>
          {props.link.icon ? 'Choose a new icon or leave blank to keep the current icon.' : 'Choose an icon or leave blank to use the default.'}
        </div>}
        <div style={{
          display: "flex",
          alignItems: "center"
        }}>
          <FileUpload name="icon" value={iconFile} onChange={e => setIconFile(e.target.value)}/>
        </div>
      </Box>
    </DialogContent>
    <DialogActions>
      <Button type='submit' form='create-link-form'>{props.link ? 'Save' : 'Create'}</Button>
    </DialogActions>
  </Dialog>
}