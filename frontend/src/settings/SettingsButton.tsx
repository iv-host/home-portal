import * as React from 'react';

import Fab from '@mui/material/Fab';
import SettingsIcon from '@mui/icons-material/Settings';
import { SettingsDialog } from './SettingsDialog'

export interface SettingsButtonProps {
  onOpen?: () => void,
  onClose?: () => void,
  onLinksUpdated?: () => void,
}

export const SettingsButton = (props: SettingsButtonProps) => {
  const [open, setOpen] = React.useState(false)
  
  const handleOpen = () => {
    setOpen(true)
    
    if(props.onOpen) {
      props.onOpen()
    }
  }

  const handleClose = () => {
    setOpen(false)

    if(props.onClose) {
      props.onClose()
    }
  }

  return (
    <>
      <Fab size='medium' onClick={handleOpen}>
        <SettingsIcon/>
      </Fab>
      <SettingsDialog 
        open={open}
        onClose={handleClose}
        onLinksUpdated={props.onLinksUpdated}
      />
    </>
  );
}