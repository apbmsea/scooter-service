import { call, put, takeLatest } from 'typed-redux-saga';
import {
	recoveryFailure,
	recoveryRequest,
	recoverySuccess
} from './recoverySlice';
import { recovery } from '../../../entities/recovery/recovery.api';
import type { RecoveryPayload } from '../../../entities/recovery/recovery.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { isHandledError } from '@shared/utils/isHandledError';
import { navigateTo } from '@shared/utils/navigate';
import { local } from '@shared/utils/localStorageHelper';
function* recoverySaga(action: PayloadAction<RecoveryPayload>) {
	try {
		const response = yield* call(recovery, action.payload);
		const { accessToken } = response;
		localStorage.setItem('accessToken', accessToken);
		yield* put(recoverySuccess());
		yield* call(navigateTo, '/auth/login');
		local.set('email', action.payload.email);
	} catch (error: unknown) {
		if (isHandledError(error)) {
			yield* put(recoveryFailure(error.data.errors));
		} else {
			yield* put(recoveryFailure({}));
		}
	}
}

export function* watchRecovery() {
	yield* takeLatest(recoveryRequest.type, recoverySaga);
}
