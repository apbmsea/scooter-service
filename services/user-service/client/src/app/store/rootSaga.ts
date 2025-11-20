import { watchLogin } from '@pages/LoginPage/features/login';
import { watchLogout } from '@features/logout';
import { watchRecovery } from '@pages/RecoveryPage/features/recovery';
import { watchRefresh } from '@features/refresh';
import { watchRegister } from '@pages/RegisterPage/features/register';
import { watchUser } from '@features/user';
import { all } from 'typed-redux-saga';

export default function* rootSaga() {
	yield all([watchRegister(), watchLogin(), watchRefresh(), watchLogout(), watchRecovery(), watchUser()]);
}
