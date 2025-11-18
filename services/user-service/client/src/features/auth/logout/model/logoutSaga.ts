import { call, put, takeLatest } from 'typed-redux-saga';
import { logoutFailure, logoutRequest, logoutSuccess } from './logoutSlice';
import { logout } from '../api/logout.api';
import { resetUser } from '@entities/user';

function* logoutSaga() {
	try {
		yield* call(logout);
		localStorage.removeItem('accessToken');
		yield* put(resetUser());
		yield* put(logoutSuccess());
	} catch (error: unknown) {
		yield* put(logoutFailure());
		console.log(error);
	}
}

export function* watchLogout() {
	yield* takeLatest(logoutRequest.type, logoutSaga);
}
