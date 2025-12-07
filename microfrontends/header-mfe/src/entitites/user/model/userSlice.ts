import { type PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import { type User } from 'scooter-shared';

interface UserState {
	user: User | null;
	isLoading: boolean;
}

const initialState: UserState = {
	user: null,
	isLoading: false
};

const userSlice = createSlice({
	name: 'user',
	initialState,
	reducers: {
		userRequest: state => {
			state.isLoading = true;
		},
		userSuccess: (state, action: PayloadAction<User>) => {
			state.user = action.payload;
			state.isLoading = false;
		},
		userFailure: state => {
			state.user = null;
			state.isLoading = false;
		}
	}
});

export const { userSuccess, userRequest, userFailure } = userSlice.actions;
export default userSlice.reducer;
