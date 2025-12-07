require('dotenv').config();

module.exports = {
	server: {
		hostIp: process.env.HOST_IP,
		hostUri: process.env.HOST_URI,
		port: parseInt(process.env.SERVER_PORT, 10) || 5000
	},
	ssl: {
		certDir: process.env.SSL_CERT_DIR || 'certs',
		certFile: process.env.SSL_CERT_FILE || 'server.crt',
		keyFile: process.env.SSL_KEY_FILE || 'server.key'
	},
	gateway: {
		url: process.env.GATEWAY_URL
	},
	mfe: {
		ports: {
			header: parseInt(process.env.MFE_HEADER_PORT, 10) || 3000,
			home: parseInt(process.env.MFE_HOME_PORT, 10) || 3001,
			auth: parseInt(process.env.MFE_AUTH_PORT, 10) || 3002,
			user: parseInt(process.env.MFE_USER_PORT, 10) || 3003,
			admin: parseInt(process.env.MFE_ADMIN_PORT, 10) || 3004,
			monitoring: parseInt(process.env.MFE_MONITORING_PORT, 10) || 3005,
		},
		assets: {
			path: process.env.MFE_ASSETS_PATH || '/assets',
			cssFile: process.env.MFE_CSS_FILE || 'main.css',
			jsFile: process.env.MFE_JS_FILE || 'main.js'
		}
	},
	api: {
		timeout: parseInt(process.env.API_TIMEOUT, 10) || 10000,
		refreshTokenTimeout:
			parseInt(process.env.REFRESH_TOKEN_TIMEOUT, 10) || 5000
	}
};
