import { $api, type User } from "@scooter/shared";

export async function getIUser() {
  const response = await $api.get(`/users/me`);
  return response.data;
}

export async function updateIUser(user: User) {
  const response = await $api.put(`/users/me`, user);
  return response.data;
}
