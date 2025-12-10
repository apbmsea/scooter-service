import { $api } from "scooter-shared";

export async function getUsers() {
  const response = await $api.get(`/users`);
  return response.data;
}

export async function deleteUser(id: string) {
  const response = await $api.delete(`/users/${id}`);
  return response.data;
}
