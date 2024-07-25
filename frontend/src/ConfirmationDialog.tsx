import * as React from 'react';

import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import CircularProgress from '@mui/material/CircularProgress';


interface ConfirmationDialogProps {
  open: boolean
  busy?: boolean
  onClose: (agreed: boolean) => void
  title: string
  message: string
  onAnimationEnd?: React.AnimationEventHandler<HTMLDivElement>
}

export const ConfirmationDialog = (props: ConfirmationDialogProps) => {
  const { open, busy = false, onClose, title, message, onAnimationEnd} = props

  return (
      <Dialog
        open={open}
        onAnimationEnd={onAnimationEnd}
      >
        <DialogTitle>
          { title }
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            { message }
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          {!busy && <>
            <Button onClick={() => onClose(false)}>Disagree</Button>
            <Button onClick={() => onClose(true)} autoFocus>Agree</Button>
          </>}
          {busy && <CircularProgress size={32} />}
        </DialogActions>
      </Dialog>
  );
}