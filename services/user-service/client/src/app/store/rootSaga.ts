import { watchLogin } from '@features/auth/login';
import { watchLogout } from '@features/auth/logout';
import { watchRecovery } from '@features/auth/recovery';
import { watchRefresh } from '@features/auth/refresh';
import { watchRegister } from '@features/auth/register';
import { all } from 'typed-redux-saga';

export default function* rootSaga() {
	yield all([watchRegister(), watchLogin(), watchRefresh(), watchLogout(), watchRecovery()]);
}
