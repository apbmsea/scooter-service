import { $api } from '@shared/api/instance';

export async function logout() {
	const response = await $api.post(`/auth/logout`);
	return response.data;
}
