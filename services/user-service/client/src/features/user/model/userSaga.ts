import { call, put, takeLatest } from 'typed-redux-saga';
import { getIUser, updateIUser } from '../api/user.api';
import {
	getIUserFailure,
	getIUserRequest,
	getIUserSuccess,
	updateIUserFailure,
	updateIUserRequest,
	updateIUserSuccess
} from './userSlice';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { User } from './user.types';
import { navigateTo } from '@shared/utils/navigate';

function* getIUserSaga() {
	try {
		const response = yield* call(getIUser);
		const { user } = response;
		yield* put(getIUserSuccess(user));
	} catch (error: unknown) {
		yield* put(getIUserFailure());
		console.log(error);
	}
}

function* updateIUserSaga(action: PayloadAction<User>) {
	try {
		const response = yield* call(updateIUser, action.payload);
		const { user } = response;
		yield* put(updateIUserSuccess(user));
		yield* call(navigateTo, '/home');
	} catch (error: unknown) {
		yield* put(updateIUserFailure({}));
		console.log(error);
	}
}

export function* watchUser() {
	yield* takeLatest(getIUserRequest.type, getIUserSaga);
	yield* takeLatest(updateIUserRequest.type, updateIUserSaga);
}
