import { isLinkVisibleToRoles } from "./LinkRoleUtils"
import type { Link } from "../services/LinksService"

const link = (roles: string[]): Link => ({
  name: "Portal",
  href: "https://example.com",
  roles
})

test("shows links with no role restrictions", () => {
  expect(isLinkVisibleToRoles(link([]), [])).toBe(true)
})

test("shows restricted links when the user has any configured role", () => {
  expect(isLinkVisibleToRoles(link(["admin", "support"]), ["support"])).toBe(true)
})

test("hides restricted links when the user has none of the configured roles", () => {
  expect(isLinkVisibleToRoles(link(["admin", "support"]), ["user"])).toBe(false)
})
