import { $api } from '@scooter/shared';

export async function logout() {
	const response = await $api.post(`/auth/logout`);
	return response.data;
}
