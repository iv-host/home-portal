import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { SettingsButton } from './settings/SettingsButton';
import { BackgroundImage, Link } from './services/LinksService';
import { SearchBar } from './SearchBar';


const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#1976d2',
    },
  },
});

const lightTheme = createTheme({
  palette: {
    mode: 'light'
  },
});



export interface SearchAppBarProps {
  links: Link[],
  onLinksUpdated?: () => void,
  background: BackgroundImage | undefined,
  onSelectBackground?: (bg: BackgroundImage | undefined) => void
}

export default function SearchAppBar(props: SearchAppBarProps) {
  const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches

  return (
    <Box sx={{ flexGrow: 1 }}>
      <ThemeProvider theme={isDark ? darkTheme : lightTheme}>
        <AppBar position="static">
          <Toolbar variant="dense">
            <SettingsButton {...props} />
            <Typography
              variant="h6"
              noWrap
              component="div"
              sx={{ flexGrow: 1, display: { xs: 'none', sm: 'block' } }}
            >
              Home Portal
            </Typography>
            <SearchBar links={props.links}/>
          </Toolbar>
        </AppBar>
      </ThemeProvider>
    </Box>
  );
}
