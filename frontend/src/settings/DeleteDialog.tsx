import * as React from 'react';
import { ConfirmationDialog } from '../ConfirmationDialog';

export interface DeleteDialogProps<Item> {
  item?: Item
  open: boolean,
  onClose?: () => void,
  title?: string,
  message?: string
  onAnimationEnd?: React.AnimationEventHandler<HTMLDivElement>
  doDelete?: (item: Item | undefined) => Promise<void>
}

export const DeleteDialog = <Item,>(props: DeleteDialogProps<Item>) => {
  const { open, onClose=()=>{}, title="Delete", message="Are you sure?" } = props
  const [busy, setBusy] = React.useState<boolean>(false);

  React.useEffect(() => {
    if(open) {
      setBusy(false)
    }
  }, [open])

  const handleDelete = (accept: boolean) => {
    if(accept && props.doDelete) {
      setBusy(true)
      props.doDelete(props.item).then(()=>onClose())
    } else {
      onClose()
    }
  }

  return <ConfirmationDialog
    open = {open}
    busy = {busy}
    title = {title}
    message = {message}
    onAnimationEnd={props.onAnimationEnd}
    onClose={handleDelete}
  />
}