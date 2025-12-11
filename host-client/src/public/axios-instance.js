(function () {
    'use strict';
    function initAxiosInstance() {
        if (typeof window.axios === 'undefined') {
            console.error('axios не загружен, перед использованием проверьте что он загрузился');
            return;
        }
        const config = {
            baseURL: window.__GATEWAY_URL__ || 'https://10.4.3.81:8081',
            withCredentials: true,
            timeout: window.__API_TIMEOUT__ || 10000,
            headers: {
                'Content-Type': 'application/json'
            }
        };
        const apiInstance = window.axios.create(config);
        const refreshInstance = window.axios.create(config);
        const handleError = (error, context = '') => {
            if (!error.response) {
                if (error.code) {
                    console.error(`${error.code}${context}`);
                }
                return Promise.reject(error);
            }
            const handledError = {
                status: error.response.status,
                data: error.response.data
            };
            if (handledError.status === 401 || handledError.status === 403) {
                console.warn(`Ошибка авторизации${context}`);
            }
            else {
                console.error(`Ошибка ${handledError.status}${context}`, handledError);
            }
            return Promise.reject(handledError);
        };
        refreshInstance.interceptors.response.use((response) => response, (error) => handleError(error, ' при обновлении токена'));
        apiInstance.interceptors.request.use((config) => {
            const token = localStorage.getItem('accessToken');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        }, (error) => {
            console.error(error);
            return Promise.reject(error);
        });
        apiInstance.interceptors.response.use((response) => response, async (error) => {
            const originalRequest = error.config;
            if (!error.response) {
                return handleError(error);
            }
            const handledError = {
                status: error.response.status,
                data: error.response.data
            };
            if (handledError.status === 401 && !originalRequest._isRetry) {
                originalRequest._isRetry = true;
                if (window.scooterShared?.event) {
                    window.scooterShared.event.emit('refresh');
                }
            }
            return Promise.reject(handledError);
        });
        if (typeof window !== 'undefined') {
            window.__API__ = apiInstance;
            window.__REFRESH__ = refreshInstance;
            window.$refresh = refreshInstance;
        }
    }
    if (typeof window.axios !== 'undefined') {
        initAxiosInstance();
    }
    else {
        if (typeof window !== 'undefined') {
            window.addEventListener('load', function () {
                if (typeof window.axios !== 'undefined') {
                    initAxiosInstance();
                }
                else {
                    console.error('axios не смог загрузиться');
                }
            });
        }
    }
})();
