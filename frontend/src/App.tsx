import React, { useEffect, useState } from 'react';
import './App.css';
import { Link } from './link/Link';
import { InitInfo, LinkService } from './services/LinksService';
import { getConfig } from "./config/config";

function App() {
  const [ initInfo, setInitInfo] = useState<InitInfo>({background:undefined, links:[]})
  const { service } = getConfig()
  
  useEffect(() => {
    const load = async () => {
      const initInfo = await LinkService.getInit();
      setInitInfo(initInfo)
    }
    load()
  }, [])

  return (
    <div className="App" style={{
      backgroundImage: initInfo.background ? `url(${service.host + initInfo.background.url})` : "unset"
    }}>
      {initInfo.links.map(function(link, i) {
        return <Link key={link.href} name={link.name} href={link.href} icon={link.icon ? service.host+link.icon : undefined}/>
      })}
    </div>
  );
}

export default App;
