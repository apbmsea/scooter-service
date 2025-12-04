import { useEffect, useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../app/store/store';
import { userRequest } from '../../entitites/user/model/userSlice';
import { event, navigateTo, type User } from '@scooter/shared';
import './Header.scss';

interface Link {
	href: string;
	label: string;
}

const USER_LINKS: Link[] = [
	{ href: '/home', label: 'Главная' },
	{ href: '/auth', label: 'Карта' },
	{ href: '/info', label: 'Информация' }
];

const OPERATOR_LINKS: Link[] = [
	{ href: '/link', label: 'link' },
	{ href: '/link', label: 'link' },
	{ href: '/link', label: 'link' }
];

const ADMIN_LINKS: Link[] = [
	{ href: '/admin-panel', label: 'Админ панель' },
	{ href: '/statistics', label: 'Статистика' },
	{ href: '/server-info', label: 'Состояние серверов' }
];

const ROLE_LINKS: Record<User['role'], Link[]> = {
	USER: USER_LINKS,
	ADMIN: ADMIN_LINKS,
	OPERATOR: OPERATOR_LINKS
};

const Header = () => {
	const dispatch = useDispatch();
	const user = useSelector((state: RootState) => state.user);

	useEffect(() => {
		dispatch(userRequest());

		const unsubscribe = event.on('user_update', () => {
			dispatch(userRequest());
		});

		return unsubscribe;
	}, [dispatch]);

	const links = useMemo(() => {
		const role = user?.role ?? 'USER';
		return ROLE_LINKS[role];
	}, [user?.role]);

	return (
		<header className='header'>
			<a className='header__logo' href='/home'>
				Самокат
			</a>

			<nav className='header__navbar'>
				{links.map(link => (
					<a
						key={link.href}
						href={link.href}
						className='header__navbar-link'
					>
						{link.label}
					</a>
				))}
			</nav>

			<div className='header__buttons'>
				{user ? (
					<>
						<button
							onClick={() => navigateTo('/users/me')}
							className='header__buttons-default'
						>
							Настройки
						</button>
						<button
							onClick={() => event.emit('logout')}
							className='header__buttons-logout'
						>
							Выход
						</button>
					</>
				) : (
					<>
						<button
							onClick={() => navigateTo('/auth/login')}
							className='header__buttons-default'
						>
							Вход
						</button>
						<button
							onClick={() => navigateTo('/auth/register')}
							className='header__buttons-secondary'
						>
							Регистрация
						</button>
					</>
				)}
			</div>
		</header>
	);
};

export default Header;
