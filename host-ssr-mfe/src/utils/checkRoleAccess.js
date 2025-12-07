const { getUserRole } = require('./getUserRole');
const endpointsConfig = require('../configs/endpoints.config');
const { ROLES } = require('../constants');

async function checkRoleAccess(path, accessToken) {
	const endpoint = endpointsConfig.endpoints.find(e => e.path === path);

	if (!endpoint || !endpoint.allowedRoles || endpoint.allowedRoles.length === 0) {
		return { allowed: true, userRole: null };
	}

	let userRole;
	try {
		userRole = await getUserRole(accessToken);
	} catch (error) {
		console.error('Ошибка получения роли пользователя:', error);
		userRole = ROLES.UNAUTHORIZED;
	}

	return {
		allowed: endpoint.allowedRoles.includes(userRole),
		userRole: userRole
	};
}

module.exports = { checkRoleAccess };