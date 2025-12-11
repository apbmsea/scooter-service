import axios from "axios";

declare global {
  interface Window {
    __API__?: typeof axios;
    __REFRESH__?: typeof axios;
  }
}

function getRefreshInstance() {
  if (typeof window !== "undefined" && window.__REFRESH__) {
    return window.__REFRESH__;
  }
  console.error("Рефреш инстанс работает в дев режиме!")
  return axios.create({});
}

function getApiInstance() {
  if (typeof window !== "undefined" && window.__API__) {
    return window.__API__;
  }
  console.error("Инстанс работает в дев режиме!")
  return axios.create({});
}

export const $api = getApiInstance();
export const $refresh = getRefreshInstance();
