import { getConfig } from "../config/config";
import { Cookies, parseCookies } from "../utils/CookieUtils";
import { OAuth2ClientService } from "./OAuth2ClientService";
import { ServiceResponse, createResponse } from "./ServiceResponse"

export interface BackgroundImage {
  filename: string,
  url: string
}

export interface Link {
  name: string,
  href: string,
  icon?: string
}

export interface UserInfo {
  features: {settings: boolean}
}

class LinksServiceImpl {
  host: string;
  apiUrl: string;
  cookies: Cookies;
  
  constructor() {
    const { service } = getConfig();
    this.host = service.host;
    this.apiUrl = `${service.host}${service.path}`;

    this.cookies = parseCookies()
  }
  
  async getVersion(): Promise<ServiceResponse<string>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + '/info/version', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/text'
        })
      });

      return createResponse(response, async () => await response.text())
    })
  }

  async addBackground(form: FormData): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + '/bg', {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getRandomBackground(): Promise<ServiceResponse<BackgroundImage>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + '/bg/random', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })

      return createResponse(response, async () => {
        const bg = (await response.json()) as BackgroundImage
        bg.url = this.host + bg.url

        return bg
      })
    })
  }

  async getBackgrounds(): Promise<ServiceResponse<BackgroundImage[]>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + '/bg', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })

      return createResponse(response, async () => {
        const bgs = (await response.json()) as BackgroundImage[]
        bgs.forEach(bg => bg.url = this.host + bg.url)

        return bgs
      })
    })
  }

  async deleteBackground(name: string): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + `/bg/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getLinks(): Promise<ServiceResponse<Link[]>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + '/links', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      });

      return createResponse(response, async () => {
        const links = (await response.json()) as Link[]
        links.forEach(link => link.icon = link.icon ? this.host + link.icon : undefined)

        return links
      })
    })
  }

  async deleteLink(name: string): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + `/links/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async createLink(name: string, form: FormData): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + `/links/${name}`, {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getUserInfo(): Promise<ServiceResponse<UserInfo>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.apiUrl + `/user`, {
        method: 'GET',
        headers: this.headers(authorization)
      });

      return createResponse(response, async ()=> (await response.json()) as UserInfo)
    })
  }

  private headers(authorization: string | undefined, headers: HeadersInit | undefined = undefined): HeadersInit {
    const myHeaders:Headers = new Headers(headers)
    
    if(authorization) {
      myHeaders.set('Authorization', authorization)
    }

    return myHeaders
  }
}

export const LinkService = new LinksServiceImpl()