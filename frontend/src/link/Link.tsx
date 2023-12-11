import React from 'react';
import './Link.css';
import noimage from './noimage.png'

export interface LinkProps {
  name: string;
  href: string;
  icon?: string;
}

export const Link = ({ name, href, icon }: LinkProps) => {
  
  const click = () => {
    window.location.href = href;
  }
  
  return <>
    <div className='link' tabIndex={0} onClick={click}>
      <div className='image'>
        <div className='icon'>
          <img src={icon ? icon : noimage} draggable={false}/>
        </div>
      </div>
      <div className='name'>
        {name}
      </div>
    </div>
  </>
}