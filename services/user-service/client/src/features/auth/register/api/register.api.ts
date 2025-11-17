import { $api } from '@shared/api/instance';
import type { RegisterPayload } from '../model/register.types';

export async function register(payload: RegisterPayload) {
	const response = await $api.post(`/auth/register`, payload);
	return response.data;
}