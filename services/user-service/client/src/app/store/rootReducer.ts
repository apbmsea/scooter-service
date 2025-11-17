import { registerReducer } from '@features/auth/register';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	register: registerReducer
});

export default rootReducer;
