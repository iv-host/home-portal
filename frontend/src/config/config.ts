import configs from './config.json'

export const ENVIRONMENT = process.env.NODE_ENV ? process.env.NODE_ENV : "production"

export interface Environments {
  [key: string]: Properties
}

export interface Properties {
  service: {host: string, path: string}
}

export const getConfig = () => {
  const config = (configs as Environments)[ENVIRONMENT]
  if(config) {
    return config;
  } else {
    throw `Unknown Environment: ${ENVIRONMENT}`
  }
}