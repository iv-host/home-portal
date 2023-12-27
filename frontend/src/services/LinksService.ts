import { getConfig } from "../config/config";

export interface BackgroundImage {
  filename: string,
  url: string
}

export interface Link {
  name: string,
  href: string,
  icon?: string
}

class MyLinksService {
  url: string;
  
  constructor() {
    const { service } = getConfig();
    this.url = `${service.host}${service.path}`;
  }
  
  async getVersion(): Promise<string> {
    const response = await fetch(this.url + '/info/version', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/text'
      }
    });

    return await response.text()
  }

  async getRandomBackground(): Promise<BackgroundImage> {
    const response = await fetch(this.url + '/bg/random', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    return (await response.json()) as BackgroundImage
  }

  async getLinks(): Promise<Link[]> {
    const response = await fetch(this.url + '/links', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    return (await response.json()) as Link[]
  }

  async deleteLink(name: string): Promise<void> {
    const response = await fetch(this.url + `/links/${name}`, {
      method: 'DELETE',
    });

    if(!response.ok) {
      throw response.statusText
    }
  }

  async createLink(name: string, form: FormData): Promise<void> {
    const response = await fetch(this.url + `/links/${name}`, {
      method: 'POST',
      body: form
    });

    if(!response.ok) {
      throw response.statusText
    }
  }
}

export const LinkService = new MyLinksService()