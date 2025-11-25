import { $api } from "sharedMF/api";
import type { LoginPayload } from './login.types';

export async function login(payload: LoginPayload) {
	const response = await $api.post(`/auth/login`, payload);
	return response.data;
}
