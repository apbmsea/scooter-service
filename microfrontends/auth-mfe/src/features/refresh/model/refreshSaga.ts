import { call, put, takeLatest, take } from 'typed-redux-saga';
import { refreshFailure, refreshRequest, refreshSuccess } from './refreshSlice';
import { refresh } from '@entities/refresh/refresh.api';
import { event } from 'scooter-shared';
import { eventChannel } from 'redux-saga';

function* refreshSaga() {
	try {
		const response = yield* call(refresh);
		const { accessToken } = response;
		localStorage.setItem('accessToken', accessToken);
		yield* put(refreshSuccess());
		event.emit('user_update')
	} catch (error: unknown) {
		yield* put(refreshFailure());
		console.log(error);
	}
}

export function createRefreshChannel() {
	return eventChannel(emit => {
		const handler = () => emit('REFRESH_EVENT');

		event.on('refresh', handler);

		return () => {};
	});
}


export function* watchRefresh() {
	yield* takeLatest(refreshRequest.type, refreshSaga);

	const channel = yield* call(createRefreshChannel);

	while (true) {
		yield* take(channel);
		yield* put(refreshRequest());
	}
}
