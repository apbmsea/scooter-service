export interface User {
	id: string;
	email: string;
	firstName: string;
	lastName: string;
	phone: string;
	role: 'USER' | 'OPERATOR' | 'ADMIN';
	createdAt: string;
	updatedAt: string;
}
