import { call, put, takeLatest } from "typed-redux-saga";
import { getIUser, updateIUser } from "../../../entities/user/user.api";
import {
  getIUserFailure,
  getIUserRequest,
  getIUserSuccess,
  updateIUserFailure,
  updateIUserRequest,
  updateIUserSuccess,
} from "./userSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import { type User, isHandledError, event } from "scooter-shared";

function* getIUserSaga() {
  try {
    const response = yield* call(getIUser);
    yield* put(getIUserSuccess(response));
  } catch (error: unknown) {
    yield* put(getIUserFailure());
    console.log(error);
  }
}

function* updateIUserSaga(
  action: PayloadAction<
    Omit<User, "id" | "role" | "createdAt" | "updatedAt"> & {
      password?: string;
    }
  >
) {
  try {
    const payload = { ...action.payload };
    if (!payload.password || payload.password.trim() === "") {
      delete payload.password;
    }

    const response = yield* call(updateIUser, payload as User);
    yield* put(updateIUserSuccess(response));
    event.emit("user_update");
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
