import type { Role } from '@shared/types/role.types';

export interface User {
	id: string;
	email: string;
	firstName: string;
	lastName: string;
	phone: string;
	role: Role;
	createdAt: string;
	updatedAt: string;
}
