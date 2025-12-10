export type AppEventMap = {
  logout: void;
  refresh: void;
  user_update: void;
};

declare global {
  interface Window {
    scooterShared?: {
      event: TypedEventEmitter<AppEventMap>;
    };
  }
}

type EventName = keyof AppEventMap;
class TypedEventEmitter<M extends Record<string, unknown>> {
  emit<K extends keyof M>(eventName: K, detail?: M[K]): void {
    if (typeof window === "undefined") return;

    window.dispatchEvent(
      new CustomEvent(String(eventName as EventName), {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        detail: detail as any,
      })
    );
  }

  on<K extends keyof M>(
    eventName: K,
    callback: (detail: M[K]) => void
  ): () => void {
    if (typeof window === "undefined") {
      return () => {};
    }

    const handler = (event: Event) => {
      const customEvent = event as CustomEvent<M[K]>;
      callback(customEvent.detail);
    };

    window.addEventListener(String(eventName as EventName), handler);

    return () => {
      window.removeEventListener(String(eventName as EventName), handler);
    };
  }
}

export const event = new TypedEventEmitter<AppEventMap>();

if (typeof window !== "undefined") {
  window.scooterShared = { event };
}
