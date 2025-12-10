import axios from 'axios';
import https from 'https';
import { envConfig } from '../configs/env.config';
import { ROLES, ROLE_MAP, Role } from '../constants';

export async function getUserRole(accessToken?: string): Promise<Role> {
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
				httpsAgent: new https.Agent({ rejectUnauthorized: false })
			}
		);
		if (userResponse.status !== 200) {
			return ROLES.UNAUTHORIZED;
		}

		return ROLE_MAP[userResponse.data.role as keyof typeof ROLE_MAP] || ROLES.UNAUTHORIZED;
	} catch (error: any) {
		console.error('Ошибка получения роли пользователя:', error.message);
		return ROLES.UNAUTHORIZED;
	}
}