import React from 'react';
import './LinkCard.css';
import noimage from './noimage.png'

export interface LinkCardProps {
  name: string;
  href: string;
  icon?: string;
}

export const LinkCard = ({ name, href, icon }: LinkCardProps) => {
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