import axios from 'axios';

export const $auth = axios.create({
	baseURL: import.meta.env.VITE_SERVER_URL,
	timeout: 10000
});

export const $api = axios.create({
	baseURL: import.meta.env.VITE_SERVER_URL,
	withCredentials: true,
	timeout: 10000
});

$api.interceptors.request.use(config => {
	const token = localStorage.getItem('accessToken');
	if (token) {
		config.headers.Authorization = `Bearer ${token}`;
	}
	return config;
});