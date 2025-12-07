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
			allowedRoles: ['admin'],
			meta: {
				title: 'Восстановление пароля'
			},
			microfrontends: ['header', 'auth']
		},

		//user-mfe
		{
			path: '/users/me',
			allowedRoles: ['unauthorized', 'user', 'operator', 'admin'],
			meta: {
				title: 'Настройки'
			},
			microfrontends: ['header', 'user']
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
