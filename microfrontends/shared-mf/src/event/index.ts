export const event = {
  emit: <T = any>(event: string, detail?: T) => {
    window.dispatchEvent(new CustomEvent(event, { detail }));
  },
  on: <T = any>(event: string, callback: (detail?: T) => void) => {
    const handler = (e: CustomEvent) => callback(e.detail as T);
    window.addEventListener(event, handler as EventListener);
    return () => window.removeEventListener(event, handler as EventListener);
  },
};
