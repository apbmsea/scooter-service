import { call, put, takeLatest } from 'typed-redux-saga';
import { getIUser, updateIUser } from '../../../entities/user/user.api';
import {
	getIUserFailure,
	getIUserRequest,
	getIUserSuccess,
	updateIUserFailure,
	updateIUserRequest,
	updateIUserSuccess
} from './userSlice';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { User } from '../../../entities/user/user.types';
import { navigateTo } from '@shared/utils/navigate';
import { isHandledError } from 'sharedMF/utils';
import { event } from 'sharedMF/event';

function* getIUserSaga() {
	try {
		const user = yield* call(getIUser);
		yield* put(getIUserSuccess(user));
	} catch (error: unknown) {
		yield* put(getIUserFailure());
		console.log(error);
	}
}

function* updateIUserSaga(action: PayloadAction<User>) {
	try {
		const user = yield* call(updateIUser, action.payload);
		yield* put(updateIUserSuccess(user));
		event.emit('user_update')
		yield* call(navigateTo, '/home');
	} catch (error: unknown) {
		if (isHandledError(error)) {
			yield* put(updateIUserFailure(error.data.errors));
		} else {
			yield* put(updateIUserFailure({}));
		}
	}
}

export function* watchUser() {
	yield* takeLatest(getIUserRequest.type, getIUserSaga);
	yield* takeLatest(updateIUserRequest.type, updateIUserSaga);
}
