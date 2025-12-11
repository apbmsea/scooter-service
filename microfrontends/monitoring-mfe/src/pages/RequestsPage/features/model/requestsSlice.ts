import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { type Request } from "@pages/RequestsPage/entities/requests.types";

interface RequestsState {
  requests: Request[];
  isLoading: boolean;
}

const initialState: RequestsState = {
  requests: [],
  isLoading: false,
};

const requestsSlice = createSlice({
  name: "requests",
  initialState,
  reducers: {
    getRequestsRequest: (state) => {
      state.isLoading = true;
    },
    getRequestsSuccess: (state, action: PayloadAction<Request[]>) => {
      state.isLoading = false;
      state.requests = action.payload;
    },
    getRequestsFailure: (state) => {
      state.isLoading = false;
    },
  },
});

export const { getRequestsRequest, getRequestsSuccess, getRequestsFailure } =
  requestsSlice.actions;
export default requestsSlice.reducer;
