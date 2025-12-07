const axios = require('axios');
const envConfig = require('../configs/env.config');
const { ROLES, ROLE_MAP } = require('../constants');

async function getUserRole(refreshToken) {
	if (!refreshToken) {
		return ROLES.UNAUTHORIZED;
	}

	try {
		const response = await axios.get(`${envConfig.gateway.url}/users/me`, {
			headers: {
				Cookie: `refreshToken=${refreshToken}`
			},
			timeout: envConfig.api.timeout,
			validateStatus: status => status < 500
		});

		if (response.status !== 200) {
			return ROLES.UNAUTHORIZED;
		}

		return ROLE_MAP[response.data.role] || ROLES.UNAUTHORIZED;
	} catch (error) {
		console.error('Ошибка получения роли пользователя:', error.message);
		return ROLES.UNAUTHORIZED;
	}
}

module.exports = { getUserRole };

