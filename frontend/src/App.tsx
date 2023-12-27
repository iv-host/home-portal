import React, { useEffect, useState } from 'react';
import './App.css';
import { LinkCard } from './link/LinkCard';
import { Link, BackgroundImage, LinkService } from './services/LinksService';
import { getConfig } from "./config/config";
import { SettingsButton } from './settings/SettingsButton';

function App() {
  const [ links, setLinks ] = useState<Link[]>([])
  const [ background, setBackground] = useState<BackgroundImage>()

  const { service } = getConfig()

  const loadLinks = async () => {
    const newLinks = await LinkService.getLinks()
    setLinks(newLinks)
  }

  const loadBackground = async () => {
    const newBackground = await LinkService.getRandomBackground()
    setBackground(newBackground)
  }

  useEffect(() => {
    loadBackground()
    loadLinks()
  }, [])

  return (
    <div className="App" style={{
      backgroundImage: background ? `url(${service.host + background.url})` : "unset",
      backgroundRepeat: "no-repeat",
      backgroundSize: "cover"
    }}>
      {links.map(function(link, i) {
        return <LinkCard key={link.href} name={link.name} href={link.href} icon={link.icon ? service.host+link.icon : undefined}/>
      })}
      
      <div><SettingsButton onLinksUpdated={()=> {loadLinks()}}/></div>  
    </div>
  );
}

export default App;
