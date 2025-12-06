import { $api } from '@scooter/shared';
import type { RecoveryPayload } from './recovery.types';

export async function recovery(payload: RecoveryPayload) {
    const response = await $api.post(`/auth/forgot-password`, payload);
    return response.data;
}
