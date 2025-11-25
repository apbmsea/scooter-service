import { $api } from "sharedMF/api";
import type { RegisterPayload } from './register.types';

export async function register(payload: RegisterPayload) {
	const response = await $api.post(`/auth/register`, payload);
	return response.data;
}
