import React from "react"
import { BackgroundImage, LinkService } from "../services/LinksService"
import Typography from "@mui/material/Typography"
import { styled } from '@mui/material/styles';
import Avatar from '@mui/material/Avatar';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import Fab from '@mui/material/Fab';
import Tooltip from '@mui/material/Tooltip';
import { DeleteDialog, DeleteDialogProps } from "./DeleteDialog";
import { AddBackgroundForm } from "./AddBackgroundForm";
import { request } from "../services/ServiceResponse";
import { nop } from "../utils/FunctionUtils";

const ImageCardContainer = styled("div")(({ theme }) => ({
  display: "flex",
  flexWrap: "wrap",
}));

const ImageCard = styled("div")(({ theme }) => ({
  display: "grid",
  alignContent: "center",
  justifyContent: "center",
  margin: "8px",
  cursor: "pointer",
  
  "&.selected": {
    margin: "5px",
    border: "solid",
    borderWidth: "3px",
    borderColor: theme.palette.primary.light,
    borderRadius: theme.shape.borderRadius,
  }
}));

interface BackgroundCardProps {
  background: BackgroundImage,
  selected?: boolean,
  onSelectBackground?: (bg: BackgroundImage) => void,
  onDelete?: (bg: BackgroundImage) => void,
  height: number,
  width: number,
}

const BackgroundCard = (props: BackgroundCardProps) => {
  const { selected=false } = props
  
  const handleClick = () => {
    if(props.onSelectBackground) {
      props.onSelectBackground(props.background)
    }
  }

  const handleClickDelete = () => {
    if(props.onDelete) {
      props.onDelete(props.background)
    }
  }

  return <div 
    style={{position: "relative"}}>

    <ImageCard className={selected ? "selected" : ""} onClick={handleClick}>
      <div style={{display: "grid", justifyContent: "center"}}>
        <div
          style={{
            height: `${props.height}px`,
            width: `${props.width}px`,
            backgroundImage: `url('${props.background.url}')`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
          }}/>
        
      </div>
    </ImageCard>
    <div style={{position: "absolute", top: 0, right: 0}}>
      <Tooltip title="Delete">
        <Fab size="small" onClick={handleClickDelete}><DeleteIcon/></Fab>
      </Tooltip>
    </div>
  </div>
}

interface CreateNewImageCardProps {
  upload: (form: FormData)=>Promise<void>
  height: number,
  width: number,
}

const CreateNewImageCard = (props: CreateNewImageCardProps) => {

  return <AddBackgroundForm {...props} >
    <ImageCard style={{height: `${props.height}px`, width: `${props.width}px`}} >
      <div style={{display: "grid", justifyContent: "center"}}>
        <Avatar>
          <AddIcon/>
        </Avatar>
      </div>
      <div><Typography fontSize={"small"}>Add Background</Typography></div>
    </ImageCard>
  </AddBackgroundForm>
}

export interface BackgroundsTabProps {
  background: BackgroundImage | undefined,
  onSelectBackground?: (bg: BackgroundImage | undefined) => void,
  onError?: (msg: string) => void
}

export const BackgroundsTab = (props: BackgroundsTabProps) => {
  const { onError } = props
  const [backgrounds, setBackgrounds] = React.useState<BackgroundImage[]>()
  const [deleteDialogProps, setDeleteDialogProps] = React.useState<DeleteDialogProps<{bg: BackgroundImage, index: number}>>({
    open: false
  })

  const loadBackgrounds = React.useCallback(() => {
    return request(LinkService.getBackgrounds(),
        bgs => setBackgrounds(bgs),
        msg => onError ? onError(msg) : nop()
    );
  }, [onError]);

  const compare = (bg1: BackgroundImage, bg2: BackgroundImage | undefined) => {
    return bg2 !== undefined
      && bg1.filename===bg2.filename 
      && bg1.url===bg2.url
  }

  const handleClickDelete = (bg: BackgroundImage, index: number) => {
    setDeleteDialogProps({
      open: true,
      item: {bg: bg, index: index},
      title: "Delete Background",
      message: "Are you sure you want to delete this background?",
      onClose: handleCloseDeleteDialog,
      doDelete: handleDelete,
    })
  }

  const handleUpload = async (form: FormData) => await request(LinkService.addBackground(form),
    async _ => await loadBackgrounds(),
    msg=>props.onError ? props.onError(msg) : nop());

  const handleDelete = async (item: {bg: BackgroundImage, index: number} | undefined) => {
    const {bg, index} = item!
    
    await request(LinkService.deleteBackground(bg.filename),
      undefined,
      msg => props.onError ? props.onError(msg) : {}
    )

    const backgrounds = await loadBackgrounds()
    
    if(props.onSelectBackground) {
      if(backgrounds===undefined || backgrounds.length === 0 ) {
        props.onSelectBackground(undefined)
      } else if(index >= backgrounds.length) {
        props.onSelectBackground(backgrounds[backgrounds.length-1])
      } else {
        props.onSelectBackground(backgrounds[index])
      }
    }
  }

  const handleCloseDeleteDialog = () => {
    setDeleteDialogProps({open: false})
  }

  React.useEffect(()=> {
    loadBackgrounds().catch(e=>console.error(e))
  }, [loadBackgrounds])

  const imgWidth = 160
  const imgHeight = (9.0/16) * imgWidth

  return <>
    <DeleteDialog {...deleteDialogProps} />
    <ImageCardContainer>
      {backgrounds && backgrounds.map((bg, index) => {
        return <BackgroundCard
          key={`${bg.filename}`}
          height={imgHeight}
          width={imgWidth}
          background={bg}
          onSelectBackground={props.onSelectBackground}
          onDelete={() => handleClickDelete(bg, index)}
          selected={compare(bg, props.background)} />
      })}
      <CreateNewImageCard 
          height={imgHeight}
          width={imgWidth}
          upload={handleUpload}/>
    </ImageCardContainer>
  </>
}