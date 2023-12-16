import React from 'react';
import './Link.css';
import noimage from './noimage.png'

export interface LinkProps {
  name: string;
  href: string;
  icon?: string;
}

export const Link = ({ name, href, icon }: LinkProps) => {
  return <>
    <a className='link' tabIndex={0} href={href}>
      <div className='image'>
        <div className='icon'>
          <img src={icon ? icon : noimage} draggable={false}/>
        </div>
      </div>
      <div className='name'>
        {name}
      </div>
    </a>
  </>
}