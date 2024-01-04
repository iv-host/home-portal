import { jwtDecode } from "jwt-decode";
import { expireCookie, parseCookies } from "../utils/CookieUtils";
import { ServiceResponse, createLoginResponse } from "./ServiceResponse";

const ACCESS_TOKEN_COOKIE = "access-token"
const REFRESH_TOKEN_COOKIE = "refresh-token"
const LATENCY_EXPIRATION_BUFFER = 5 // seconds given for latency and/or systemu

const LOGIN_PATH = "/oauth2/login"
const REFRESH_PATH = "/oauth2/refresh"

interface OAuth2TokenResponse {
    access_token?: string,
    expires_in?: number,
    refresh_expires_in?: number,
    refresh_token?: string,
    token_type?: string,
    'not-before-policy'?: number,
    session_state?: string,
    scope?: string
}

class OAuth2ClientServiceImpl {

  refreshSync: Promise<any> | undefined

  constructor() {
    const cookies = parseCookies()
    const accessToken = cookies[ACCESS_TOKEN_COOKIE]
    if (accessToken) {
      this.setAccessToken(accessToken)
      expireCookie(ACCESS_TOKEN_COOKIE)
    }

    const refreshToken = cookies[REFRESH_TOKEN_COOKIE]
    if(refreshToken) {
      this.setRefreshToken(refreshToken)
      expireCookie(REFRESH_TOKEN_COOKIE)
    }
  }

  async call<T>(request: (authorization:string | undefined)=>Promise<ServiceResponse<T>>): Promise<ServiceResponse<T>> {
    if(!this.getAccessToken() && !this.getAccessToken()) {
      // oauth may be disabled

      const response = await request(undefined)
      if(this.loginOnUnauthorized(response)) {
        return createLoginResponse()
      }

      // If the response wasn't a 401, then everything is good. And if not good, it's not an OAuth issue
      return response
    }

    if(this.isExpired(this.getAccessToken())) {
      if(this.isExpired(this.getRefreshToken())) {
        this.login()
      } else {
        await this.refresh()
      }
    } 
      
    const response = await request(`Bearer ${this.getAccessToken()}`)
    this.loginOnUnauthorized(response)
    if(this.loginOnUnauthorized(response)) {
      return createLoginResponse()
    }

    return response
  }

  private async refresh() {
    if(!this.refreshSync) {
      this.refreshSync = this.doRefresh()
      await this.refreshSync
      this.refreshSync = undefined
    } else {
      await this.refreshSync
    }
  }

  private async doRefresh() {

    const response = await fetch(`${REFRESH_PATH}?refresh_token=${this.getRefreshToken()}`, {
      method: 'GET'
    })

    const tokenResponse = await response.json() as OAuth2TokenResponse
    this.setAccessToken(tokenResponse.access_token)
    this.setRefreshToken(tokenResponse.refresh_token)
  }

  private setAccessToken(accessToken: string | undefined) {
    if(accessToken) {
      localStorage.setItem(ACCESS_TOKEN_COOKIE, accessToken)
    } else {
      localStorage.removeItem(ACCESS_TOKEN_COOKIE)
    }
  }

  private getAccessToken(): string | undefined {
    const accessToken = localStorage.getItem(ACCESS_TOKEN_COOKIE)
    return  accessToken ? accessToken : undefined
  }

  private setRefreshToken(refreshToken: string | undefined) {
    if(refreshToken) {
      localStorage.setItem(REFRESH_TOKEN_COOKIE, refreshToken)
    } else {
      localStorage.removeItem(REFRESH_TOKEN_COOKIE)
    }
  }

  private getRefreshToken(): string | undefined {
    const refreshToken = localStorage.getItem(REFRESH_TOKEN_COOKIE)
    return  refreshToken ? refreshToken : undefined
  }

  private login() {
    window.location.href = LOGIN_PATH
  }

  private isExpired(token: string | undefined): boolean {
    if(!token) {
      return true
    }

    const jwtPayload = jwtDecode(token)
    const exp = jwtPayload.exp
    if(!exp) {
      return true
    }

    return Date.now() / 1000 >= exp - LATENCY_EXPIRATION_BUFFER
  }

  private isUnauthorized(response: ServiceResponse<any>): boolean {
    return response.status === 401
  }

  private loginOnUnauthorized(response: ServiceResponse<any>): boolean {
    if(this.isUnauthorized(response)) {
      this.login()
      return true
    } else {
      return false
    }
  }
};

export const OAuth2ClientService = new OAuth2ClientServiceImpl()