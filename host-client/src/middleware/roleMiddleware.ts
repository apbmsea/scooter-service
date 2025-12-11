import { Request, Response, NextFunction } from 'express';
import { getUserRole } from '../utils/getUserRole';
import { endpointsConfig } from '../configs/endpoints.config';
import { ROLES, PATHS } from '../constants';

export async function roleMiddleware(req: Request, res: Response, next: NextFunction): Promise<void> {
	if (req.path.startsWith('/api') || req.path.includes('.')) {
		return next();
	}

	try {
		const endpoint = endpointsConfig.endpoints.find(e => e.path === req.path);

		if (!endpoint) {
			return next();
		}

		if (!endpoint.allowedRoles || endpoint.allowedRoles.length === 0) {
			return next();
		}
		const accessToken = req.headers.authorization?.replace('Bearer ', '');
		
		let userRole;
		try {
			userRole = await getUserRole(accessToken);
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