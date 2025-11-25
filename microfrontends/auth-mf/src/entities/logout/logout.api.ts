import { $api } from "sharedMF/api";

export async function logout() {
	const response = await $api.post(`/auth/logout`);
	return response.data;
}
