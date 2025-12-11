import createSagaMiddleware from 'redux-saga';
import { configureStore } from '@reduxjs/toolkit';
import { userReducer, watchUser } from '../../entitites/user';

const sagaMiddleware = createSagaMiddleware();

export const store = configureStore({
	reducer: userReducer,
	middleware: getDefaultMiddleware =>
		getDefaultMiddleware({ thunk: false }).concat(sagaMiddleware)
});

sagaMiddleware.run(watchUser);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;