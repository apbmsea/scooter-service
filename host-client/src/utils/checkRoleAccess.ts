import { getUserRole } from './getUserRole';
import { endpointsConfig } from '../configs/endpoints.config';
import { ROLES, Role } from '../constants';

interface RoleAccessResult {
	allowed: boolean;
	userRole: Role | null;
}

export async function checkRoleAccess(path: string, accessToken?: string): Promise<RoleAccessResult> {
	const endpoint = endpointsConfig.endpoints.find(e => e.path === path);

	if (!endpoint || !endpoint.allowedRoles || endpoint.allowedRoles.length === 0) {
		return { allowed: true, userRole: null };
	}

	let userRole: Role;
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