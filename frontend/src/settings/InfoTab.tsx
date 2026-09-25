import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import InfoIcon from '@mui/icons-material/Info';
import ApiIcon from '@mui/icons-material/Api';
import DownloadIcon from '@mui/icons-material/Download';
import RestoreIcon from '@mui/icons-material/Restore';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { ReactElement } from 'react';
import styled from '@emotion/styled'
import { LinkService } from '../services/LinksService';
import { request } from '../services/ServiceResponse';
import { nop } from '../utils/FunctionUtils';
import {getConfig} from "../config/config";
import { BackupService } from '../services/BackupService';
import { ConfirmationDialog } from '../ConfirmationDialog';

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
  const { onError } = props
  const [version, setVersion] = React.useState<string>()
  const [restoreFile, setRestoreFile] = React.useState<File>()
  const [restoreOpen, setRestoreOpen] = React.useState(false)
  const [restoreBusy, setRestoreBusy] = React.useState(false)
  const { service } = getConfig()
  const swaggerUrl = `${service.host}/swagger-ui/index.html`

  const showError = React.useCallback((message: string) => {
    onError ? onError(message) : nop()
  }, [onError])

  React.useEffect(() => {
    request(LinkService.getVersion(),
      v => setVersion(v),
      showError
    )
  }, [showError])

  const handleBackup = async () => {
    await request(BackupService.download(), backup => {
      if (!backup) {
        return
      }

      const url = URL.createObjectURL(backup)
      const anchor = document.createElement("a")
      anchor.href = url
      anchor.download = "home-portal-backup.zip"
      document.body.appendChild(anchor)
      anchor.click()
      anchor.remove()
      URL.revokeObjectURL(url)
    }, showError)
  }

  const handleRestoreSelected = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    event.target.value = ""
    if (file) {
      setRestoreFile(file)
      setRestoreOpen(true)
    }
  }

  const handleRestoreConfirmation = async (accepted: boolean) => {
    if (!accepted) {
      setRestoreOpen(false)
      setRestoreFile(undefined)
      return
    }

    setRestoreBusy(true)
    let restored = false
    await request(
      BackupService.restore(restoreFile!),
      () => {
        restored = true
      },
      showError,
    )

    if (restored) {
      window.location.reload()
      return
    }

    setRestoreBusy(false)
    setRestoreOpen(false)
    setRestoreFile(undefined)
  }

  return <>
    <ConfirmationDialog
      open={restoreOpen}
      busy={restoreBusy}
      title="Restore Backup"
      message={`Restore ${restoreFile?.name}? This replaces all existing links, link icons, and background images.`}
      onClose={handleRestoreConfirmation}
    />
    <UnorderedList>
      <li>
        <Item icon={<InfoIcon/>}>Build: {version}</Item>
      </li>
      <li>
        <Item icon={<ApiIcon/>}>Open Api: <a target='blank' href={swaggerUrl}>{swaggerUrl}</a></Item>
      </li>
      <li>
        <Item icon={<DownloadIcon/>}>
          <Button startIcon={<DownloadIcon/>} onClick={handleBackup}>Download Backup</Button>
        </Item>
      </li>
      <li>
        <Item icon={<RestoreIcon/>}>
          <Button component="label" startIcon={<RestoreIcon/>}>
            Restore Backup
            <input hidden type="file" accept=".zip,application/zip" onChange={handleRestoreSelected}/>
          </Button>
        </Item>
      </li>
    </UnorderedList>
  </>
}