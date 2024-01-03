import React, { useEffect, useState } from 'react';
import './App.css';
import { LinkCard } from './link/LinkCard';
import { Link, BackgroundImage, LinkService, UserInfo } from './services/LinksService';
import { getConfig } from "./config/config";
import SearchAppBar from './SearchAppBar';

function App() {
  const [ userInfo, setUserInfo] = useState<UserInfo>()
  const [ links, setLinks ] = useState<Link[]>([])
  const [ background, setBackground] = useState<BackgroundImage>()

  const { service } = getConfig()

  const loadUserInfo = async () => {
    const newUserInfo = await LinkService.getUserInfo()
    setUserInfo(newUserInfo)
  }

  const loadLinks = async () => {
    const newLinks = await LinkService.getLinks()
    setLinks(newLinks)
  }

  const loadBackground = async () => {
    const newBackground = await LinkService.getRandomBackground()
    setBackground(newBackground)
  }

  useEffect(() => {
    loadUserInfo()
    loadBackground()
    loadLinks()
  }, [])

  return (<div className='App'>
    <SearchAppBar 
      enableSettings={ userInfo ? userInfo.features.settings : false }
      links={links}
      onLinksUpdated={loadLinks}
      background={background}
      onSelectBackground={setBackground} />
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
        {links.map(function(link, i) {
          return <LinkCard key={link.href} name={link.name} href={link.href} icon={link.icon ? service.host+link.icon : undefined}/>
        })}
    </div>
  </div>);
}

export default App;
