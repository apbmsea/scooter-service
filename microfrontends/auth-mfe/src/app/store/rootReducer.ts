import { logoutReducer } from '@features/logout';
import { refreshReducer } from '@features/refresh';
import { loginReducer } from '@pages/LoginPage/features/login';
import { recoveryReducer } from '@pages/RecoveryPage/features/recovery';
import { registerReducer } from '@pages/RegisterPage/features/register';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	register: registerReducer,
	login: loginReducer,
	recovery: recoveryReducer,
	logout: logoutReducer,
	refresh: refreshReducer
});

export default rootReducer;
