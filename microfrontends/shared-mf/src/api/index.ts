import axios from "axios";
import type { HandledError } from "../types/index";
import { event } from "../event/index";

export const $api = axios.create({
  baseURL: import.meta.env.VITE_GATEWAY_URI,
  withCredentials: true,
  timeout: 10000,
});

export const $refresh = axios.create({
  baseURL: import.meta.env.VITE_GATEWAY_URI,
  withCredentials: true,
  timeout: 10000,
});

$refresh.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.status == 401) {
      event.emit("navigate", {
        path: "/auth/login",
      });
    }

    return Promise.reject(error);
  }
);

event.emit("navigate", {
  path: "/auth/login",
});

$api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

$api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (!error.response) {
      switch (error.code) {
        case "ERR_NETWORK":
          console.error("ERR_NETWORK");
          break;
        case "ECONNABORTED":
          console.error("ECONNABORTED");
          break;
        case "ERR_CANCELED":
          console.error("ERR_CANCELED");
          break;
      }

      return Promise.reject(error);
    }

    const handledError: HandledError = {
      status: error.response.status,
      data: error.response.data,
    };

    switch (handledError.status) {
      case 400:
        return Promise.reject(handledError);

      case 401:
        if (!originalRequest._isRetry) {
          originalRequest._isRetry = true;

          event.emit("refresh");
        }
        break;

      case 403:
        console.warn("Доступ запрещён");
        break;

      case 404:
        console.warn("Ресурс не найден");
        break;

      case 500:
        console.error("Ошибка сервера");
        break;

      default:
        console.error("Необработанная ошибка", handledError);
    }
    return Promise.reject(handledError);
  }
);
