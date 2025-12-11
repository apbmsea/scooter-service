import { envConfig } from './env.config';

export interface MicrofrontendConfig {
	name: string;
	path: string;
}

export const microfrontendsConfig = {
	microfrontends: [
		{ name: 'header', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.header}` },
		{ name: 'home', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.home}` },
		{ name: 'auth', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.auth}` },
		{ name: 'user', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.user}` },
		{ name: 'admin', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.admin}` },
		{ name: 'monitoring', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.monitoring}` }
	] as MicrofrontendConfig[]
};