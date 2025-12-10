const axios = require('axios');
const envConfig = require('../configs/env.config');
const { ROLES, ROLE_MAP } = require('../constants');

async function getUserRole(accessToken) {
	if (!accessToken) {
		return ROLES.UNAUTHORIZED;
	}

	try {
		const userResponse = await axios.get(
			`${envConfig.gateway.url}/users/me`,
			{
				headers: {
					Authorization: `Bearer ${accessToken}`
				},
				withCredentials: true,
				timeout: envConfig.api.timeout,
				validateStatus: status => status < 500,
				httpsAgent: new (require('https').Agent)({ rejectUnauthorized: false })
			}
		);
		if (userResponse.status !== 200) {
			return ROLES.UNAUTHORIZED;
		}

		return ROLE_MAP[userResponse.data.role] || ROLES.UNAUTHORIZED;
	} catch (error) {
		console.error('Ошибка получения роли пользователя:', error.message);
		return ROLES.UNAUTHORIZED;
	}
}

module.exports = { getUserRole };
