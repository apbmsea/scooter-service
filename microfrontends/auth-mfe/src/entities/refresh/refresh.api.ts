import { $refresh } from "@scooter/shared";

export async function refresh() {
    const response = await $refresh.post(`/auth/refresh`);
    return response.data;
}