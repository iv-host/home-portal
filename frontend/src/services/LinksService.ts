import { getConfig } from "../config/config";
import { Cookies, parseCookies } from "../utils/CookieUtils";
import { OAuth2ClientService } from "./OAuth2ClientService";

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
  
  async getVersion(): Promise<string> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + '/info/version', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/text'
        })
      });
    })

    return await response.text()
  }

  async addBackground(form: FormData): Promise<void> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + '/bg', {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });
    })

    if(!response.ok) {
      throw response.statusText
    }
  }

  async getRandomBackground(): Promise<BackgroundImage> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + '/bg/random', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })
    })

    return (await response.json()) as BackgroundImage
  }

  async getBackgrounds(): Promise<BackgroundImage[]> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + '/bg', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      })
    })

    return (await response.json()) as BackgroundImage[]
  }

  async deleteBackground(name: string): Promise<void> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + `/bg/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });
    })

    if(!response.ok) {
      throw response.statusText
    }
  }

  async getLinks(): Promise<Link[]> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + '/links', {
        method: 'GET',
        headers: this.headers(authorization, {
          'Content-Type': 'application/json'
        })
      });
    })

    return (await response.json()) as Link[]
  }

  async deleteLink(name: string): Promise<void> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + `/links/${name}`, {
        method: 'DELETE',
        headers: this.headers(authorization)
      });
    })

    if(!response.ok) {
      throw response.statusText
    }
  }

  async createLink(name: string, form: FormData): Promise<void> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + `/links/${name}`, {
        method: 'POST',
        body: form,
        headers: this.headers(authorization)
      });
    })

    if(!response.ok) {
      throw response.statusText
    }
  }

  async getUserInfo(): Promise<UserInfo> {
    const response = await OAuth2ClientService.call(async (authorization) => {
      return fetch(this.url + `/user`, {
        method: 'GET',
        headers: this.headers(authorization)
      });
    })

    return (await response.json()) as UserInfo
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