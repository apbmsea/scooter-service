import { call, put, takeLatest, take } from 'typed-redux-saga';
import { logoutFailure, logoutRequest, logoutSuccess } from './logoutSlice';
import { logout } from '@entities/logout/logout.api';
import { event, navigateTo } from 'scooter-shared';
import { eventChannel } from 'redux-saga';

function* logoutSaga() {
	try {
		yield* call(logout);
		localStorage.removeItem('accessToken');
		yield* put(logoutSuccess());
		event.emit('user_update');
		yield* call(navigateTo, "/home")
	} catch (error: unknown) {
		yield* put(logoutFailure());
		console.log(error);
	}
}

export function createLogoutChannel() {
	return eventChannel(emit => {
		const handler = () => emit('LOGOUT_EVENT');

		event.on('logout', handler);

		return () => {};
	});
}

export function* watchLogout() {
	yield* takeLatest(logoutRequest.type, logoutSaga);
	const channel = yield* call(createLogoutChannel);

	while (true) {
		yield* take(channel);
		yield* put(logoutRequest());
	}
}
