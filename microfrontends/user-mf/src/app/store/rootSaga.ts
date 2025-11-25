import { watchUser } from '@pages/UserPage/features/user';
import { all } from 'typed-redux-saga';

export default function* rootSaga() {
	yield all([watchUser()]);
}
