import { call, put, takeLatest } from 'typed-redux-saga';
import { refreshFailure, refreshRequest, refreshSuccess } from './refreshSlice';
import { refresh } from '../../../entities/refresh/refresh.api';
import { event } from 'sharedMF/event';

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

export function* watchRefresh() {
	yield* takeLatest(refreshRequest.type, refreshSaga);
}
