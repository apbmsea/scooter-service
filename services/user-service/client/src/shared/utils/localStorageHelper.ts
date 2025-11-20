export const local = {
  set<T>(key: string, value: T): void {
    try {
      const serialized = JSON.stringify(value);
      localStorage.setItem(key, serialized);
    } catch (err) {
      console.error(`localStorage set error for key "${key}":`, err);
    }
  },

  get<T>(key: string): T | null {
    try {
      const item = localStorage.getItem(key);
      if (!item) return null;
      return JSON.parse(item) as T;
    } catch (err) {
      console.error(`localStorage get error for key "${key}":`, err);
      return null;
    }
  },

  remove(key: string): void {
    try {
      localStorage.removeItem(key);
    } catch (err) {
      console.error(`localStorage remove error for key "${key}":`, err);
    }
  }
};
