import { call, put, takeLatest } from 'typed-redux-saga';
import { getUser } from './user.api';
import { userFailure, userRequest, userSuccess } from './userSlice';

function* userSaga() {
	try {
		const response = yield* call(getUser);
		yield* put(userSuccess(response.user));
	} catch (error: unknown) {
		yield* put(userFailure());
		console.log(error);
	}
}

export function* watchUser() {
	yield* takeLatest(userRequest.type, userSaga);
}