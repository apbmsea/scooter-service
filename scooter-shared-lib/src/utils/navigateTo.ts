declare global {
  interface Window {
    clientRouter?: {
      navigate: (path: string) => void;
    };
  }
}

export const navigateTo = (path: string) => {
  if (typeof window !== "undefined" && window.clientRouter) {
    window.clientRouter.navigate(path);
    setTimeout(() => {
      window.dispatchEvent(
        new CustomEvent("locationchange", {
          detail: { path: window.location.pathname },
        })
      );
    }, 0);
  } else {
    window.history.pushState({ path }, "", path);
    window.dispatchEvent(
      new CustomEvent("locationchange", { detail: { path } })
    );
  }
};
