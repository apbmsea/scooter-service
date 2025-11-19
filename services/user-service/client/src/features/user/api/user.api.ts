import { $api } from '@shared/api/instance';
import type { User } from '../model/user.types';

export async function getIUser() {
	const response = await $api.post(`/user/me`);
	return response.data;
}

export async function updateIUser(user: User) {
	const response = await $api.put(`/user/me`, user);
	return response.data;
}
