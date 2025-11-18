import { userReducer } from '@entities/user';
import { loginReducer } from '@features/auth/login';
import { logoutReducer } from '@features/auth/logout';
import { refreshReducer } from '@features/auth/refresh';
import { registerReducer } from '@features/auth/register';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	register: registerReducer,
	login: loginReducer,
	refresh: refreshReducer,
	logout: logoutReducer,
	user: userReducer
});

export default rootReducer;
