import express from 'express';
import cors from 'cors';
import cookieParser from 'cookie-parser';
import path from 'path';
import http from 'http';

const { envConfig } = require('./configs/env.config');
const indexRouter = require('./routes/index.router');

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

http.createServer(app).listen(envConfig.server.port, () => {
	console.log(`Сервер запущен на порту ${envConfig.server.port}`);
});