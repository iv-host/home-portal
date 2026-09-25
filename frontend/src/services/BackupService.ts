import { getConfig } from "../config/config";
import { OAuth2ClientService } from "./OAuth2ClientService";
import { ServiceResponse, createResponse } from "./ServiceResponse";

class BackupServiceImpl {
  private readonly apiUrl: string;

  constructor() {
    const { service } = getConfig();
    this.apiUrl = `${service.host}${service.path}/backup`;
  }

  async download(): Promise<ServiceResponse<Blob>> {
    return OAuth2ClientService.call(async authorization => {
      const response = await fetch(this.apiUrl, {
        method: "GET",
        headers: this.headers(authorization),
      });

      return createResponse(response, async () => await response.blob());
    });
  }

  async restore(file: File): Promise<ServiceResponse<void>> {
    const form = new FormData();
    form.set("file", file);

    return OAuth2ClientService.call(async authorization => {
      const response = await fetch(`${this.apiUrl}/restore`, {
        method: "POST",
        body: form,
        headers: this.headers(authorization),
      });

      return createResponse(response);
    });
  }

  private headers(authorization: string | undefined): HeadersInit {
    const headers = new Headers();
    if (authorization) {
      headers.set("Authorization", authorization);
    }
    return headers;
  }
}

export const BackupService = new BackupServiceImpl();
