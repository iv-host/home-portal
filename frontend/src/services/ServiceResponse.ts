import { nop } from "../utils/FunctionUtils"

export interface ServiceResponse<T> {
  body?: T
  ok: boolean
  status: number
  statusText: string
  loginRequired: boolean
};

export const createLoginResponse = <T,>():ServiceResponse<T> => {
  return {
    body: undefined,
    ok: false,
    status: -401,
    statusText: "login required",
    loginRequired: true
  }
}

export const createResponse = async <T,>(response: Response, body: (() => Promise<T>) | undefined = undefined):Promise<ServiceResponse<T>> => {
  return {
    body: body && response.ok ? await body() : undefined,
    ok: response.ok,
    status: response.status,
    statusText: response.statusText,
    loginRequired: false
  }
}

export const request = async <T,>(request: Promise<ServiceResponse<T>>, success: ((t:T | undefined)=>void) | undefined , error: ((msg: string)=>void) | undefined): Promise<T | undefined> => { 
  try {
    const response = await request
    
    if(response.ok) {
      success ? success(response.body) : nop()
      return response.body
    } else {
      if(response.loginRequired) {
        // do nothing, the user will be redirected to the login page
      } else {
        const message = `Service Error: ${response.status} ${response.statusText}`
        console.error(message)
        
        error ? error(message) : nop()
      }
      return undefined
    }
  } catch (e: unknown) {
    console.error(e)
    error ? error("Service Error: Unknown Error") : nop()

    return undefined
  }
}
