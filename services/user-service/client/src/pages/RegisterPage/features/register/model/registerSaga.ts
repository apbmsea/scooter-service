import { call, put, takeLatest } from 'typed-redux-saga';
import {
	registerFailure,
	registerRequest,
	registerSuccess
} from './registerSlice';
import { register } from '../../../entities/register/register.api';
import type { RegisterPayload } from '../../../entities/register/register.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { isHandledError } from '@shared/utils/isHandledError';
import { navigateTo } from '@shared/utils/navigate';
import { local } from '@shared/utils/localStorageHelper';

function* registerSaga(action: PayloadAction<RegisterPayload>) {
	try {
		yield* call(register, action.payload);
		yield* put(registerSuccess());
		yield* call(navigateTo, '/auth/login');
		local.set('email', action.payload.email);
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
