import { userReducer } from '@pages/UserPage/features/user';
import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
	user: userReducer
});

export default rootReducer;
