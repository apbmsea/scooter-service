export const ROLES = {
	UNAUTHORIZED: 'unauthorized',
	USER: 'user',
	OPERATOR: 'operator',
	ADMIN: 'admin'
} as const;

export const ROLE_MAP = {
	USER: 'user',
	OPERATOR: 'operator',
	ADMIN: 'admin'
} as const;

export const PATHS = {
	HOME: '/home',
	NOT_FOUND: '/404'
} as const;

export const MFE_SUFFIX = {
	ROOT: '-mfe-root',
	ASSETS: '-mfe-assets',
	MOUNT: '_MFE_MOUNT__',
	UNMOUNT: '_MFE_UNMOUNT__'
} as const;

export type Role = typeof ROLES[keyof typeof ROLES];