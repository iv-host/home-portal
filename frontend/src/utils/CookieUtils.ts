
export interface Cookies {
  [key: string]: string | undefined
}

export const parseCookies = ():Cookies => {

  const cookies:Cookies = {}
  document.cookie.split(';').forEach(c => {
    if(!c) {
      return
    }

    const nameValue = c.split('=')
    const name: string = nameValue[0].trim()
    const value: string | undefined = nameValue.length>=1 ? nameValue[1].trim() : undefined

    if(value) {
      cookies[name] = value
    }    
  })

  return cookies
}

export const expireCookie = (name: string) => {
  document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
}