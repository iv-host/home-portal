import React from 'react';
import noimage from './noimage.png'
import { styled } from '@mui/material/styles';

const LINK_CARD_WIDTH_PX = 100
const LINK_CARD_BORDER_WIDTH_PX = 3
const LINK_CARD_MARGIN_PX = 20

export const LINK_CARD_OFFSET_WIDTH = LINK_CARD_WIDTH_PX + 2*LINK_CARD_BORDER_WIDTH_PX + 2*LINK_CARD_MARGIN_PX

const LinkCardAnchor = styled('a')(({ theme }) => ({
  display: "block",
  width: `${LINK_CARD_WIDTH_PX}px`,
  height: "100px",
  border: `${LINK_CARD_BORDER_WIDTH_PX}px solid #7c7c7c`,
  borderRadius: "15px",
  cursor: "pointer",
  paddingTop: "20px",
  boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)",
  margin: `${LINK_CARD_MARGIN_PX}px`,
  backdropFilter: "blur(10px)",
  userSelect: "none",
  textDecoration: "none",

  "&:focus": {
    borderColor: "rgb(255, 255, 255)",
    boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19), 0 0 10px 0 rgba(255, 255, 255, 0.5)",
  },
}));

const LinkCardImage = styled('div')(({ theme }) => ({
  margin: "auto",
  width: "50%",
  textAlign: "center",
}));

const LinkCardIcon = styled('div')(({ theme }) => ({
  margin: "auto",
  width: "45px",
  height: "60px",
}));

const LinkCardImg = styled('img')(({ theme }) => ({
  width: "45px",
  height: "45px",
}));

const LinkCardName = styled('div')(({ theme }) => ({
  textWrap: "wrap",
  textAlign: "center",
  fontFamily: "Arial, Helvetica, sans-serif",
  color: "#ffffff",
  fontSize: "14px",
}));

export interface LinkCardProps {
  name: string;
  href: string;
  icon?: string;
}

export const LinkCard = ({ name, href, icon }: LinkCardProps) => {
  return <LinkCardAnchor tabIndex={0} href={href}>
      <LinkCardImage>
        <LinkCardIcon>
          <LinkCardImg src={icon ? icon : noimage} draggable={false}/>
        </LinkCardIcon>
      </LinkCardImage>
      <LinkCardName>
        {name}
      </LinkCardName>
    </LinkCardAnchor>
}