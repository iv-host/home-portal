import { createRef, useEffect, useState } from 'react';
import './App.css';
import { LINK_CARD_OFFSET_WIDTH, LinkCard } from './link/LinkCard';
import { Link, BackgroundImage, LinkService, UserInfo } from './services/LinksService';
import SearchAppBar from './SearchAppBar';
import Alert from '@mui/material/Alert';
import { request } from './services/ServiceResponse';
import { AlertTitle } from '@mui/material';
import { styled } from '@mui/material/styles';

const LinkContainer  = styled('div')(({ theme }) => ({
  backgroundRepeat: "no-repeat",
  backgroundSize: "cover",
  height: "100%",
  display: "flex",
  flexWrap: "wrap",
  justifyContent: "left",
  alignContent: "flex-start",
  overflow: "auto",

  [theme.breakpoints.down('sm')]: {
    display: "grid",
    gridTemplateColumns: `repeat( auto-fit, minmax(${LINK_CARD_OFFSET_WIDTH}px, 1fr))`, // 146px is the width of a single LinkCard
    justifyContent: "space-evenly",
    justifyItems: "center",
  }
}));


function App() {
  const [ error, setError] = useState<string>()
  const [ userInfo, setUserInfo] = useState<UserInfo>()
  const [ links, setLinks ] = useState<Link[]>()
  const [ background, setBackground] = useState<BackgroundImage>()

  const linkContainerRef = createRef<HTMLDivElement>()

  const loadUserInfo = () => request(LinkService.getUserInfo(),
    (userInfo) => setUserInfo(userInfo),
    (msg) => setError(msg));
  

  const loadLinks = () => request(LinkService.getLinks(),
    (links) => setLinks(links),
    (msg) => setError(msg));
    

  const loadBackground = () => request(LinkService.getRandomBackground(),
    (bg) => setBackground(bg),
    (msg) => setError(msg));

  useEffect(() => {
    loadUserInfo().catch(e => setError("Error loading user info"))
    loadBackground().catch(e => setError("Error loading background"))
    loadLinks().catch(e => setError("Error loading links"))
  }, [])

  const isReady = ():boolean => {
    return userInfo!==undefined && links!==undefined && background!==undefined
  }

  return <>
    {error && <Alert
      severity="error"
      onClose={()=>{setError(undefined)}}
      style={{
        position: "absolute",
        zIndex: 2000,
        minWidth: "200px"
      }}>
      <AlertTitle>Error</AlertTitle>
      {error}
    </Alert>}
    {isReady() && <div className='App'>
      <SearchAppBar 
        enableSettings={userInfo ? userInfo.features.settings : false}
        links={links!}
        onLinksUpdated={loadLinks}
        background={background}
        onSelectBackground={setBackground}
        onError={setError} />
      <LinkContainer ref={linkContainerRef} style={{
        backgroundImage: background ? `url(${background.url})` : "unset"
      }}>
          {links!.map(function(link, i) {
            return <LinkCard key={link.href} name={link.name} href={link.href} icon={link.icon}/>
          })}
      </LinkContainer>
    </div>}
  </>;
}

export default App;
