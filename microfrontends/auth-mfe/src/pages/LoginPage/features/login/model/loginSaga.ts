import { call, put, takeLatest } from 'typed-redux-saga';
import { loginFailure, loginRequest, loginSuccess } from './loginSlice';
import { login } from '../../../entities/login/login.api';
import type { LoginPayload } from '../../../entities/login/login.types';
import type { PayloadAction } from '@reduxjs/toolkit';
import { event, isHandledError, navigateTo } from 'scooter-shared';

function* loginSaga(action: PayloadAction<LoginPayload>) {
	try {
		const response = yield* call(login, action.payload);
		const { accessToken } = response;
		localStorage.setItem('accessToken', accessToken);
		yield* put(loginSuccess());
		event.emit('user_update');
		yield* call(navigateTo, '/home');
		localStorage.setItem('email', action.payload.email);
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
