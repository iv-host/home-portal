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
  url: string;
  cookies: Cookies;
  
  constructor() {
    const { service } = getConfig();
    this.url = `${service.host}${service.path}`;

    this.cookies = parseCookies()
  }
  
  async getVersion(): Promise<ServiceResponse<string>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + '/info/version', {
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
      const response = await fetch(this.url + '/bg', {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getRandomBackground(): Promise<ServiceResponse<BackgroundImage>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + '/bg/random', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })

      return createResponse(response, async () => (await response.json()) as BackgroundImage)
    })
  }

  async getBackgrounds(): Promise<ServiceResponse<BackgroundImage[]>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + '/bg', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })

      return createResponse(response, async () => (await response.json()) as BackgroundImage[])
    })
  }

  async deleteBackground(name: string): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + `/bg/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getLinks(): Promise<ServiceResponse<Link[]>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + '/links', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      });

      return createResponse(response, async () => (await response.json()) as Link[])
    })
  }

  async deleteLink(name: string): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + `/links/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async createLink(name: string, form: FormData): Promise<ServiceResponse<void>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + `/links/${name}`, {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });

      return createResponse(response)
    })
  }

  async getUserInfo(): Promise<ServiceResponse<UserInfo>> {
    return OAuth2ClientService.call(async (authorization) => {
      const response = await fetch(this.url + `/user`, {
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