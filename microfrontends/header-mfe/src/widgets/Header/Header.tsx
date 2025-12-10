import { useEffect, useMemo, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../app/store/store';
import { userRequest } from '../../entitites/user/model/userSlice';
import { event, navigateTo, type User } from 'scooter-shared';
import './Header.scss';

interface Link {
	href: string;
	label: string;
}

const USER_LINKS: Link[] = [
	{ href: '/home', label: 'Главная' },
	{ href: '/link', label: 'Link' },
	{ href: '/link', label: 'Link' }
];

const OPERATOR_LINKS: Link[] = [
	{ href: '/link', label: 'Link' },
	{ href: '/link', label: 'Link' },
	{ href: '/link', label: 'Link' }
];

const ADMIN_LINKS: Link[] = [
	{ href: '/admin-panel', label: 'Админ панель' },
	{ href: '/monitoring/requests', label: 'Мониторинг Запросов' },
	{ href: '/statistics', label: 'Статистика' }
];

const ROLE_LINKS: Record<User['role'], Link[]> = {
	USER: USER_LINKS,
	ADMIN: ADMIN_LINKS,
	OPERATOR: OPERATOR_LINKS
};

const Header = () => {
	const dispatch = useDispatch();
	const user = useSelector((state: RootState) => state.user);
	const [isMenuOpen, setIsMenuOpen] = useState(false);

	useEffect(() => {
		dispatch(userRequest());

		const unsubscribe = event.on('user_update', () => {
			dispatch(userRequest());
		});

		return unsubscribe;
	}, [dispatch]);

	useEffect(() => {
		const handleResize = () => {
			if (window.innerWidth > 1024) {
				setIsMenuOpen(false);
			}
		};

		window.addEventListener('resize', handleResize);
		return () => window.removeEventListener('resize', handleResize);
	}, []);

	const links = useMemo(() => {
		const role = (user?.role ?? 'USER') as User['role'];
		return ROLE_LINKS[role];
	}, [user?.role]);

	const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

	return (
		<header
			className={`header ${isMenuOpen ? 'header-open' : ''}`}
		>
			<a className='header__logo' href='/home'>
				Самокат
			</a>

			<button
				className='header__burger'
				onClick={toggleMenu}
				aria-label='Меню'
			>
				<span />
				<span />
				<span />
			</button>

			<nav className='header__navbar'>
				{links.map(link => (
					<a
						key={link.href}
						href={link.href}
						className='header__navbar-link'
						onClick={() => setIsMenuOpen(false)}
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
