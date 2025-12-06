import { watchLogout } from '@features/logout';
import { watchRefresh } from '@features/refresh';
import { watchLogin } from '@pages/LoginPage/features/login';
import { watchRecovery } from '@pages/RecoveryPage/features/recovery';
import { watchRegister } from '@pages/RegisterPage/features/register';
import { all } from 'typed-redux-saga';

export default function* rootSaga() {
	yield all([
		watchRegister(),
		watchLogin(),
		watchRecovery(),
		watchLogout(),
		watchRefresh()
	]);
}
