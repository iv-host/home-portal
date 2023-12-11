import { LinkProps } from "../link/Link";
import { getConfig } from "../config/config";

export interface InitInfo {
  background?: BackgroundImage,
  links: Link[]
}

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

  async getInit(): Promise<InitInfo> {
    const response = await fetch(this.url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    return await response.json() as InitInfo
  }
}

export const LinkService = new MyLinksService()