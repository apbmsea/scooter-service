import { call, put, takeLatest } from "typed-redux-saga";
import { deleteUser, getUsers } from "../../../entities/users/users.api";
import {
  deleteUserFailure,
  deleteUserRequest,
  deleteUserSuccess,
  getUsersFailure,
  getUsersRequest,
  getUsersSuccess,
} from "./usersSlice";
import type { PayloadAction } from "@reduxjs/toolkit";

function* getUsersSaga() {
  try {
    const response = yield* call(getUsers);
    yield* put(getUsersSuccess(response));
  } catch (error: unknown) {
    yield* put(getUsersFailure());
    console.log(error);
  }
}

function* deleteUserSaga(action: PayloadAction<{ id: string }>) {
  try {
    yield* call(deleteUser, action.payload.id);
    yield* put(deleteUserSuccess());
    yield* put(getUsersRequest());
  } catch (error: unknown) {
    yield* put(deleteUserFailure());
    console.log(error);
  }
}

export function* watchUsers() {
  yield* takeLatest(getUsersRequest.type, getUsersSaga);
  yield* takeLatest(deleteUserRequest.type, deleteUserSaga);
}
