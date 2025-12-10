const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const path = require('path');
const fs = require('fs');
const https = require('https');
const envConfig = require('./configs/env.config');
const indexRouter = require('./routes/index.router');

const SSL_CERT_PATH = path.join(__dirname, envConfig.ssl.certDir, envConfig.ssl.certFile);
const SSL_KEY_PATH = path.join(__dirname, envConfig.ssl.certDir, envConfig.ssl.keyFile);

const app = express();

app.use(
	cors({
		origin: envConfig.server.hostUri,
		methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE'],
		credentials: true
	})
);

app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use('/', indexRouter);

(async () => {
	if (!fs.existsSync(SSL_CERT_PATH) || !fs.existsSync(SSL_KEY_PATH)) {
		await require('./generate-cert');
	}

	const sslOptions = {
		cert: fs.readFileSync(SSL_CERT_PATH),
		key: fs.readFileSync(SSL_KEY_PATH)
	};

	https.createServer(sslOptions, app).listen(envConfig.server.port, () => {
		console.log(`Сервер запущен ${envConfig.server.hostUri}`);
	});
})();
