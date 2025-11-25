import { store } from '@app/store/store';
import { logoutRequest } from '@features/logout';

export const logout = () => {
	store.dispatch(logoutRequest());
};