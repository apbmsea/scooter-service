import { call, put, takeLatest } from 'typed-redux-saga';
import { logoutFailure, logoutRequest, logoutSuccess } from './logoutSlice';
import { logout } from '../../../entities/logout/logout.api';
import { event } from 'sharedMF/event';

function* logoutSaga() {
	try {
		yield* call(logout);
		localStorage.removeItem('accessToken');
		yield* put(logoutSuccess());
		event.emit('user_update')
	} catch (error: unknown) {
		yield* put(logoutFailure());
		console.log(error);
	}
}

export function* watchLogout() {
	yield* takeLatest(logoutRequest.type, logoutSaga);
}
