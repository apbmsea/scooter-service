declare module 'sharedMF/api' {
	import type { AxiosInstance } from 'axios';
	export const $api: AxiosInstance;
	export const $refresh: AxiosInstance;
}

declare module 'sharedMF/utils' {
	export function isHandledError(error: unknown): error is HandledError;
}

declare module 'sharedMF/event' {
	export const event: {
		emit: <T = unknown>(event: string, detail?: T) => void;
		on: <T = unknown>(
			event: string,
			callback: (detail?: T) => void
		) => () => void;
	};
}
