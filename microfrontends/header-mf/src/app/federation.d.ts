declare module "sharedMF/api" {
  import type { AxiosInstance } from "axios";
  export const $api: AxiosInstance;
  export const $refresh: AxiosInstance;
}

declare module "sharedMF/event" {
  export const event: {
    emit: <T = unknown>(event: string, detail?: T) => void;
    on: <T = unknown>(
      event: string,
      callback: (detail?: T) => void
    ) => () => void;
  };
}

declare module "authMF/logout" {
  export const logout: () => void;
}

declare module "authMF/refresh" {
  export const refresh: () => void;
}