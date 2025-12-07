import { call, put, takeLatest } from 'typed-redux-saga';
import {
	recoveryFailure,
	recoveryRequest,
	recoverySuccess
} from './recoverySlice';
import { recovery } from '../../../entities/recovery/recovery.api';
import type { RecoveryPayload } from '../../../entities/recovery/recovery.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { event, isHandledError, navigateTo } from 'scooter-shared';

function* recoverySaga(action: PayloadAction<RecoveryPayload>) {
	try {
		const response = yield* call(recovery, action.payload);
		const { accessToken } = response;
		localStorage.setItem('accessToken', accessToken);
		yield* put(recoverySuccess());
		event.emit('user_update');
		yield* call(navigateTo, '/auth/login');
		localStorage.setItem('email', action.payload.email);
	} catch (error: unknown) {
		if (isHandledError(error)) {
			yield* put(recoveryFailure(error.data.errors));
		} else {
			yield* put(recoveryFailure({}));
			console.log(error);
		}
	}
}

export function* watchRecovery() {
	yield* takeLatest(recoveryRequest.type, recoverySaga);
}
