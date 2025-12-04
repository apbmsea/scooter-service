import { $api } from "@scooter/shared";

export async function getUser() {
	const response = await $api.get(`/users/me`);
	return response.data;
}
