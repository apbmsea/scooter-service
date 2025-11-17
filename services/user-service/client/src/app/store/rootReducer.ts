import { loginReducer } from '@features/auth/login';
import { registerReducer } from '@features/auth/register';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	register: registerReducer,
	login: loginReducer
});

export default rootReducer;
