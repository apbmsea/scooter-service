import { watchRegister } from '@features/auth/register';
import { all } from 'typed-redux-saga';

export default function* rootSaga() {
	yield all([watchRegister()]);
}
