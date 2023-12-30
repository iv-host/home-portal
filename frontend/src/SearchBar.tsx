import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import SearchIcon from '@mui/icons-material/Search';
import useAutocomplete from '@mui/material/useAutocomplete';
import { Link } from './services/LinksService';

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.05),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.15),
  },
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(1),
    width: 'auto',
  },
}));

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));

const StyledInputBase = styled('input')(({ theme }) => ({
  color: 'inherit',
  background: "none",
  borderRadius: theme.shape.borderRadius,
  borderStyle: "none",
  outline: "none",
  height: '2.5ch',
  fontSize: theme.typography.fontSize,
  fontFamily: theme.typography.fontFamily,
  fontWeight: theme.typography.fontWeightRegular,
  padding: theme.spacing(1, 1, 1, 0),
  paddingLeft: `calc(1em + ${theme.spacing(4)})`,
  transition: theme.transitions.create('width'),
  [theme.breakpoints.up('sm')]: {
    width: '15ch',
    '&:focus': {
      width: '20ch',
    },
  },
  [theme.breakpoints.down('sm')]: {
    width: "calc(100% - 50px)"
  },
}));

const Listbox = styled('ul')(({ theme }) => ({
  width: "100%",
  margin: 0,
  padding: 0,
  zIndex: 1,
  position: 'absolute',
  listStyle: 'none',
  backgroundColor: theme.palette.background.paper,
  overflow: 'auto',
  maxHeight: 200,
  border: '1px solid rgba(0,0,0,.25)',
  '& li.Mui-focused': {
    backgroundColor: '#4a8df6',
    color: 'white',
    cursor: 'pointer',
  },
  '& li:active': {
    backgroundColor: '#2977f5',
    color: 'white',
  },
}));

const ListItemAncor = styled('a')(({ theme }) => ({
  display: "flex",
  width: "100%",
  alignItems: "center",
  color: theme.palette.text.secondary,
  textDecoration: "none",
  fontSize: theme.typography.fontSize,
  fontFamily: theme.typography.fontFamily,
  fontWeight: theme.typography.fontWeightRegular,
}));

interface ListItemLinkProps {
  link: Link
}

const ListItemLink = (props: ListItemLinkProps) => {
  const { link } = props
  const { name, href, icon } = link;

  return <ListItemAncor href={href}>
    <img style={{width: "25px", height: "25px"}} src={icon} />
    <span style={{marginLeft: "15px"}}>{name}</span>
  </ListItemAncor>
}

export interface SearchBarProps {
  links: Link[]
}

export const SearchBar = (props: SearchBarProps) => {
  const handleSelect = (link: Link | null) => {
    if(link)
      window.location.href = link?.href
  }
  
  const {
    getRootProps,
    getInputProps,
    getListboxProps,
    getOptionProps,
    groupedOptions,
  } = useAutocomplete({
    id: 'search-autocomplete',
    options: props.links,
    getOptionLabel: (link) => link.name,
    onChange: (e, l) => handleSelect(l),
  });

  return <Search {...getRootProps()}>
    <SearchIconWrapper>
      <SearchIcon />
    </SearchIconWrapper>
    <StyledInputBase
      {...getInputProps()}
      placeholder="Search…"
    />
    {groupedOptions.length > 0 ? (
      <Listbox {...getListboxProps()}>
        {(groupedOptions as typeof props.links).map((option, index) => (
          <li {...getOptionProps({ option, index })}><ListItemLink link={option} /></li>
        ))}
      </Listbox>
    ) : null}
</Search>
}