import { store } from '@app/store/store';
import { refreshRequest } from '@features/refresh';

export const refresh = () => {
    store.dispatch(refreshRequest());
};