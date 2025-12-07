import { call, put, takeLatest } from "typed-redux-saga";
import { getRequests } from "../../entities/requests.api";
import {
  getRequestsFailure,
  getRequestsRequest,
  getRequestsSuccess,
} from "./requestsSlice";

function* getRequestsSaga() {
  try {
    const response = yield* call(getRequests);
    yield* put(getRequestsSuccess(response));
  } catch (error: unknown) {
    yield* put(getRequestsFailure());
    console.log(error);
  }
}


export function* watchRequests() {
  yield* takeLatest(getRequestsRequest.type, getRequestsSaga);
}
