const { getUserRole } = require('../utils/getUserRole');
const endpointsConfig = require('../configs/endpoints.config');
const { ROLES, PATHS } = require('../constants');

async function roleMiddleware(req, res, next) {
	if (req.path.startsWith('/api') || req.path.includes('.')) {
		return next();
	}

	try {
		const endpoint = endpointsConfig.endpoints.find(
			e => e.path === req.path
		);

		if (!endpoint) {
			return next();
		}

		if (!endpoint.allowedRoles || endpoint.allowedRoles.length === 0) {
			return next();
		}
		const refreshToken = req.cookies.refreshToken;
		
		let userRole;
		try {
			userRole = await getUserRole(refreshToken);
		} catch (error) {
			console.error('Ошибка получения роли пользователя:', error);
			userRole = ROLES.UNAUTHORIZED;
		}

		const isRoleAllowed = endpoint.allowedRoles.includes(userRole);

		if (!isRoleAllowed) {
			return res.redirect(PATHS.HOME);
		}
		next();
	} catch (error) {
		console.error(error);
		return res.redirect(PATHS.HOME);
	}
}

module.exports = roleMiddleware;