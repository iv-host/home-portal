import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import InfoIcon from '@mui/icons-material/Info';
import ApiIcon from '@mui/icons-material/Api';
import Typography from '@mui/material/Typography';
import { ReactElement } from 'react';
import styled from '@emotion/styled'
import { LinkService } from '../services/LinksService';
import { request } from '../services/ServiceResponse';
import { nop } from '../utils/FunctionUtils';

const ItemDiv = styled.div`
  display: flex;
  align-items: center;
  padding: 2px;
`

const UnorderedList = styled.ul`
  list-style-type: none;
  padding-left: 12px;
`


const Item = (props: React.PropsWithChildren<{icon: ReactElement}>) => {
  return <ItemDiv>
    <div>
      <Avatar>
        {props.icon}
      </Avatar>
    </div>
    <div style={{paddingLeft: "15px"}}>
      <Typography>{props.children}</Typography>
    </div>
  </ItemDiv>
}

export interface InfoTabProps {
  onError?: (msg: string) => void
}

export const InfoTab = (props: InfoTabProps) => {
  const [version, setVersion] = React.useState<string>()
  
  React.useEffect(() => {
    request(LinkService.getVersion(),
      v => setVersion(v),
      msg => props.onError ? props.onError(msg) : nop()
    )
  })


  return <UnorderedList>
    <li>
      <Item icon={<InfoIcon/>}>Build: {version}</Item>
    </li>
    <li>
      <Item icon={<ApiIcon/>}>Open Api: <a target='blank' href='/swagger-ui/index.html'>/swagger-ui/index.html</a></Item>
    </li>
  </UnorderedList>
}