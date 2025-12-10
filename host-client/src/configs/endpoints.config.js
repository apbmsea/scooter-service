module.exports = {
	endpoints: [
		{
			path: '/home',
			allowedRoles: ['unauthorized', 'user', 'operator', 'admin'],
			meta: {
				title: 'Самокат'
			},
			microfrontends: ['header', 'home']
		},

		//auth-mfe
		{
			path: '/auth/register',
			allowedRoles: ['unauthorized'],
			meta: {
				title: 'Регистрация'
			},
			microfrontends: ['header', 'auth']
		},
		{
			path: '/auth/login',
			allowedRoles: ['unauthorized'],
			meta: {
				title: 'Вход'
			},
			microfrontends: ['header', 'auth']
		},
		{
			path: '/auth/forgot-password',
			allowedRoles: ['unauthorized'],
			meta: {
				title: 'Восстановление пароля'
			},
			microfrontends: ['header', 'auth']
		},

		//user-mfe
		{
			path: '/users/me',
			allowedRoles: ['user', 'operator', 'admin'],
			meta: {
				title: 'Настройки'
			},
			microfrontends: ['header', 'user']
		},

		//admin-mfe
		{
			path: '/admin-panel',
			allowedRoles: ['admin'],
			meta: {
				title: 'Админ Панель - Пользователи'
			},
			microfrontends: ['header', 'admin']
		},

		//monitoring-mfe
		{
			path: '/monitoring/requests',
			allowedRoles: ['admin'],
			meta: {
				title: 'Мониторинг - Запросы'
			},
			microfrontends: ['header', 'monitoring']
		},

		//statica
		{
			path: '/404',
			allowedRoles: ['unauthorized', 'user', 'operator', 'admin'],
			meta: {
				title: 'Страница не найдена'
			},
			microfrontends: []
		}
	]
};
