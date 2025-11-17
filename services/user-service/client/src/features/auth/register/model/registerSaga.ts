import { call, put, takeLatest } from 'typed-redux-saga';
import {
	registerFailure,
	registerRequest,
	registerSuccess
} from './registerSlice';
import { register } from '../api/register.api';
import type { RegisterPayload } from './register.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { isHandledError } from '@shared/utils/isHandledError';
import { navigateTo } from '@shared/utils/navigate';

function* registerSaga(action: PayloadAction<RegisterPayload>) {
	try {
		yield* call(register, action.payload);
		yield* put(registerSuccess());
		yield* call(navigateTo, 'login');
	} catch (error: unknown) {
		if (isHandledError(error)) {
			yield* put(registerFailure(error.data.errors));
		} else {
			yield* put(registerFailure({}));
		}
	}
}

export function* watchRegister() {
	yield* takeLatest(registerRequest.type, registerSaga);
}
