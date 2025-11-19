import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { User } from './user.types';

interface UserState {
	user: User | null;
	isLoading: boolean;
	errors: Record<string, string>;
}

const initialState: UserState = {
	user: null,
	isLoading: false,
	errors: {}
};

const userSlice = createSlice({
	name: 'user',
	initialState,
	reducers: {
		setUser: (state, action: PayloadAction<User>) => {
			state.user = action.payload;
		},
		resetUser: state => {
			state.user = null;
		},

		getIUserRequest: (state) => {
			state.isLoading = true;
		},
		getIUserSuccess: (state, action: PayloadAction<User>) => {
			state.isLoading = false;
			state.user = action.payload;
		},
		getIUserFailure: state => {
			state.isLoading = false;
		},

		updateIUserRequest: (state, _action: PayloadAction<Omit<User, 'password' | 'id' | 'role'>>) => {
			state.isLoading = true;
		},
		updateIUserSuccess: (state, action: PayloadAction<User>) => {
			state.isLoading = false;
			state.user = action.payload;
		},
		updateIUserFailure: (
			state,
			action: PayloadAction<Record<string, string>>
		) => {
			state.isLoading = false;
			state.errors = action.payload;
		},
				clearFieldError: (state, action: PayloadAction<string>) => {
			delete state.errors[action.payload];
		}
	}
});

export const {
	setUser,
	resetUser,
	getIUserRequest,
	getIUserSuccess,
	getIUserFailure,
	updateIUserRequest,
	updateIUserSuccess,
	updateIUserFailure,
	clearFieldError
} = userSlice.actions;
export default userSlice.reducer;
