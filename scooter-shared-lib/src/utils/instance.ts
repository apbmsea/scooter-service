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

  throw new Error("Refresh not init");
}

function getApiInstance() {
  if (typeof window !== "undefined" && window.__API__) {
    return window.__API__;
  }

  throw new Error("Instance not init");
}

export const $api = getApiInstance();
export const $refresh = getRefreshInstance();
