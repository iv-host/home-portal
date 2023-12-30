import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import { styled } from '@mui/material/styles';
import { InfoTab } from './InfoTab';
import { LinkTab } from './LinksTab';
import { BackgroundsTab } from './BackgroundsTab';
import { BackgroundImage } from '../services/LinksService';


const LargeDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(0),
  },
}));

export interface SettingsDialogProps {
  open: boolean,
  onClose?: () => void,
  onLinksUpdated?: () => void,
  background: BackgroundImage | undefined,
  onSelectBackground?: (bg: BackgroundImage | undefined) => void
}

export const SettingsDialog = (props: SettingsDialogProps) => {
  const [value, setValue] = React.useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
      <LargeDialog
        maxWidth={"lg"}
        fullWidth={true}
        onClose={props.onClose}
        open={props.open}
      >
        <DialogTitle sx={{ m: 0, p: 2 }} id="settings-dialog-title">
          Settings
        </DialogTitle>
        <IconButton
          aria-label="close"
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
        <DialogContent dividers>
        
          <Box sx={{ flexGrow: 1, bgcolor: 'background.paper', display: 'flex', minHeight: 400, width: "100%"}}>
            <Tabs
              orientation="vertical"
              variant="scrollable"
              value={value}
              onChange={handleChange}
              aria-label="Vertical tabs example"
              sx={{ borderRight: 1, borderColor: 'divider' }}
            >
              <Tab label="Links" id ="vertical-tab-0" />
              <Tab label="Backgrounds" id ="vertical-tab-1" />
              <Tab label="Info" id ="vertical-tab-2" />
            </Tabs>
            <div
                role="tabpanel"
                hidden={value !== 0}
                style={{width: "100%"}}>
              <LinkTab onLinksUpdated={props.onLinksUpdated} />
            </div>
            <div
                role="tabpanel"
                hidden={value !== 1}>
              <BackgroundsTab {...props} />
            </div>
            <div
                role="tabpanel"
                hidden={value !== 2}>
              <InfoTab/>
            </div>
          </Box>
        </DialogContent>
      </LargeDialog>
  );
}