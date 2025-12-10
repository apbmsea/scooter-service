import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { RegisterPayload } from '../../../entities/register/register.types';

interface RegisterState {
	isLoading: boolean;
	errors: Record<string, string>;
}

const initialState: RegisterState = {
	isLoading: false,
	errors: {}
};

const registerSlice = createSlice({
	name: 'register',
	initialState,
	reducers: {
		registerRequest: (state, _action: PayloadAction<RegisterPayload>) => {
			state.isLoading = true;
		},
		registerSuccess: state => {
			state.isLoading = false;
		},
		registerFailure: (
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

export const { registerSuccess, registerRequest, registerFailure, clearFieldError  } =
	registerSlice.actions;
export default registerSlice.reducer;
