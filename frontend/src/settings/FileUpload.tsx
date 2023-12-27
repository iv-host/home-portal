import React from 'react';
import styled from '@emotion/styled';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import FileUploadIcon from '@mui/icons-material/FileUpload';


const FileUploadDiv = styled.div`
  border-radius: 4px;
  border: 1px;
  border-style: solid;
  padding: 8.5px 14px;
  margin: 8px;
  border-color: rgba(0, 0, 0, 0.3);
  color: rgba(0, 0, 0, 0.6);
  height: 20px;
  width: calc(100% - 64px)
`

export interface FileUploadProps {
  id?: string,
  name?: string,
  value?: string,
  onChange?: React.ChangeEventHandler<HTMLInputElement>
}

export const FileUpload = (props: FileUploadProps) => {
  const {id, name, value, onChange} = props

  return <label style={{width: "100%"}}>
    <div style={{display: "flex", alignItems: "center"}}>
      <FileUploadDiv><Typography>{value?.replace('C:\\fakepath\\', '')}</Typography></FileUploadDiv>
      <Button startIcon={<FileUploadIcon/>} disableElevation={true} disableRipple={true} component="span" variant="contained" sx = {{
        "& .MuiButton-startIcon": {
          margin: 0
        }
      }} />
    </div>
    <input id={id} name={name} type="file" hidden accept='image/*' onChange={onChange}/>
  </label>
}