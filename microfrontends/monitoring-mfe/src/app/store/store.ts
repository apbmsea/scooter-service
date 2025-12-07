import { requestsReducer, watchRequests } from '@pages/RequestsPage/features'
import createSagaMiddleware from "redux-saga";
import { configureStore } from "@reduxjs/toolkit";

const sagaMiddleware = createSagaMiddleware();

export const store = configureStore({
  reducer: requestsReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ thunk: false }).concat(sagaMiddleware),
});

sagaMiddleware.run(watchRequests);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
