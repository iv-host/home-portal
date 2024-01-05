import React from 'react';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import FileUploadIcon from '@mui/icons-material/FileUpload';


const FileUploadDiv = styled("div")(({ theme }) => ({
  cursor: "pointer",
  borderRadius: "4px",
  borderWidth: "1px",
  borderStyle: "solid",
  padding: "8.5px 14px",
  margin: "8px",
  borderColor: theme.palette.divider,
  color: theme.palette.text.disabled,
  height: "20px",
  width: "calc(100% - 64px)",

  "&:hover": {
    borderColor: theme.palette.text.secondary,
  },

  "&:focus": {
    borderColor: theme.palette.primary.main,
    borderWidth: "2px",
    margin: "7px",
  }
}))

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
      <FileUploadDiv tabIndex={0}><Typography>{value?.replace('C:\\fakepath\\', '')}</Typography></FileUploadDiv>
      <Button startIcon={<FileUploadIcon/>} disableElevation={true} disableRipple={true} component="span" variant="contained" sx = {{
        "& .MuiButton-startIcon": {
          margin: 0
        }
      }} />
    </div>
    <input id={id} name={name} type="file" hidden accept='image/*' onChange={onChange}/>
  </label>
}