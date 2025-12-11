import { $api } from "scooter-shared";

export async function getRequests() {
  const response = await $api.get(`/monitoring/get`);
  return response.data;
}
