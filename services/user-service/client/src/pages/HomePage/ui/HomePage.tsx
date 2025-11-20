import { logoutRequest } from '@features/logout';
import type { RootState } from '@shared/types/store.types';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

const HomePage = () => {
	const { user } = useSelector((state: RootState) => state.user);
	const dispatch = useDispatch();
	return (
		<main>
			<nav style={{ display: 'flex', gap: '10px' }}>
				<Link to='/auth/login'>Вход</Link>
				<Link to='/auth/register'>Регистрация</Link>
				<Link to='/user/me'>Юзер</Link>
				<Link to='/auth/forgot-password'>Восстановление пароля</Link>
			</nav>
			<p>{user ? `Добро пожаловать ${user.firstName}` : ''}</p>
			{user ? (
				<button
					onClick={() => {
						dispatch(logoutRequest());
					}}
				>
					Выйти с аккаунта
				</button>
			) : (
				''
			)}
		</main>
	);
};

export default HomePage;
