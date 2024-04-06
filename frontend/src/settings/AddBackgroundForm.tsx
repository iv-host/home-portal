import React from "react"
import { Box } from "@mui/material"

export interface AddBackgroundFormProps {
  upload: (form: FormData) => Promise<void>
}

export const AddBackgroundForm = (props: React.PropsWithChildren<AddBackgroundFormProps>) => {

  const formRef = React.createRef<HTMLFormElement>()
  const inputRef = React.createRef<HTMLInputElement>()

  const handleFileSelect = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    if(!value) {
      return;
    }

    const form = formRef.current
    if(!form) {
      throw new Error("upload form not found")
    }

    const formData = new FormData(formRef.current!);
    await props.upload(formData)

    inputRef.current!.value = ""
  }

  return <Box
    id="add-background-form"
    component="form"
    ref={formRef}
    >
    <label>
      {props.children}
      <input ref={inputRef} name="image" type="file" hidden accept='image/*' onChange={handleFileSelect}/>
    </label>
  </Box>
}