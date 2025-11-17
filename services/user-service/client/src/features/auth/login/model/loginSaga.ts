import { call, put, takeLatest } from 'typed-redux-saga';
import { loginFailure, loginRequest, loginSuccess } from './loginSlice';
import { login } from '../api/login.api';
import type { LoginPayload } from './login.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { isHandledError } from '@shared/utils/isHandledError';
import { navigateTo } from '@shared/utils/navigate';

function* loginSaga(action: PayloadAction<LoginPayload>) {
	try {
		const response = yield* call(login, action.payload);
		localStorage.setItem('accessToken', response.accessToken);
		yield* put(loginSuccess());
		yield* call(navigateTo, 'home');
	} catch (error: unknown) {
		if (isHandledError(error)) {
			yield* put(loginFailure(error.data.errors));
		} else {
			yield* put(loginFailure({}));
		}
	}
}

export function* watchLogin() {
	yield* takeLatest(loginRequest.type, loginSaga);
}
