import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { type User } from "scooter-shared";

interface UsersState {
  users: User[];
  isLoading: boolean;
}

const initialState: UsersState = {
  users: [],
  isLoading: false,
};

const usersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    getUsersRequest: (state) => {
      state.isLoading = true;
    },
    getUsersSuccess: (state, action: PayloadAction<User[]>) => {
      state.isLoading = false;
      state.users = action.payload;
    },
    getUsersFailure: (state) => {
      state.isLoading = false;
    },
    deleteUserRequest: (state, _action: PayloadAction<{ id: string }>) => {
      state.isLoading = true;
    },
    deleteUserSuccess: (state) => {
      state.isLoading = false;
    },
    deleteUserFailure: (state) => {
      state.isLoading = false;
    },
  },
});

export const {
  getUsersRequest,
  getUsersSuccess,
  getUsersFailure,
  deleteUserRequest,
  deleteUserSuccess,
  deleteUserFailure,
} = usersSlice.actions;
export default usersSlice.reducer;
