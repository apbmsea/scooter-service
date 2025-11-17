import { createEnum, type ValueOf } from '@shared/utils/enum';

export const Role = createEnum({
	USER: 'Пользователь',
	OPERATOR: 'Оператор',
	ADMIN: 'Администратор'
});

export type Role = ValueOf<typeof Role>;
