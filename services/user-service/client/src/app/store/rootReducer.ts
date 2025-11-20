import { userReducer } from '@features/user';
import { loginReducer } from '@pages/LoginPage/features/login';
import { logoutReducer } from '@features/logout';
import { recoveryReducer } from '@pages/RecoveryPage/features/recovery';
import { refreshReducer } from '@features/refresh';
import { registerReducer } from '@pages/RegisterPage/features/register';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	register: registerReducer,
	login: loginReducer,
	recovery: recoveryReducer,
	refresh: refreshReducer,
	logout: logoutReducer,
	user: userReducer
});

export default rootReducer;
