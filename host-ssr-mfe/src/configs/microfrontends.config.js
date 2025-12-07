const envConfig = require('./env.config');

module.exports = {
	microfrontends: [
		{ name: 'header', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.header}` },
		{ name: 'home', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.home}` },
		{ name: 'auth', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.auth}` },
		{ name: 'user', path: `http://${envConfig.server.hostIp}:${envConfig.mfe.ports.user}` }
	]
};
