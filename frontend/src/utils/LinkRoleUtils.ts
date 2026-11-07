import type { Link } from "../services/LinksService"

export const isLinkVisibleToRoles = (link: Link, userRoles: string[]): boolean => {
  return !link.roles?.length || link.roles.some(role => userRoles.includes(role))
}
