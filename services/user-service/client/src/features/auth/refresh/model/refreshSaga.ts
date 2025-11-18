import { call, put, takeLatest } from 'typed-redux-saga';
import { refreshFailure, refreshRequest, refreshSuccess } from './refreshSlice';
import { refresh } from '../api/refresh.api';
import { setUser } from '@entities/user';

function* refreshSaga() {
	try {
		const response = yield* call(refresh);
		const { accessToken, user } = response;
		localStorage.setItem('accessToken', accessToken);
		yield* put(setUser(user));
		yield* put(refreshSuccess());
	} catch (error: unknown) {
		yield* put(refreshFailure());
		console.log(error);
	}
}

export function* watchRefresh() {
	yield* takeLatest(refreshRequest.type, refreshSaga);
}
