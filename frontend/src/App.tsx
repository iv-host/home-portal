import React, { useEffect, useState } from 'react';
import './App.css';
import { LinkCard } from './link/LinkCard';
import { Link, BackgroundImage, LinkService, UserInfo } from './services/LinksService';
import { getConfig } from "./config/config";
import SearchAppBar from './SearchAppBar';
import Alert from '@mui/material/Alert';
import { request } from './services/ServiceResponse';
import { AlertTitle, Button } from '@mui/material';

function App() {
  const [ error, setError] = useState<string>()
  const [ userInfo, setUserInfo] = useState<UserInfo>()
  const [ links, setLinks ] = useState<Link[]>()
  const [ background, setBackground] = useState<BackgroundImage>()

  const { service } = getConfig()

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
    loadUserInfo()
    loadBackground()
    loadLinks()
  }, [])

  const isReady = ():boolean => {
    return userInfo!=undefined && links!=undefined && background!=undefined
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
      <div  style={{
        backgroundImage: background ? `url(${service.host + background.url})` : "unset",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        height: "100%",
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "left",
        alignContent: "flex-start",
      }}>
          {links!.map(function(link, i) {
            return <LinkCard key={link.href} name={link.name} href={link.href} icon={link.icon ? service.host+link.icon : undefined}/>
          })}
      </div>
    </div>}
  </>;
}

export default App;
