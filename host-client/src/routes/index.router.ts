import express from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware';
import { renderHTML } from '../utils/renderHTML';
import { getConfigs } from '../utils/getConfigs';
import { roleMiddleware } from '../middleware/roleMiddleware';
import { checkRoleAccess } from '../utils/checkRoleAccess';
import { microfrontendsConfig } from '../configs/microfrontends.config';
import { endpointsConfig } from '../configs/endpoints.config';
import { PATHS, MFE_SUFFIX } from '../constants';

const router = express.Router();

microfrontendsConfig.microfrontends.forEach(mfe => {
	const assetsPath = `/${mfe.name}${MFE_SUFFIX.ASSETS}`;
	router.use(
		assetsPath,
		createProxyMiddleware({
			target: mfe.path,
			changeOrigin: true,
			pathRewrite: {
				[`^${assetsPath}`]: ''
			}
		})
	);
});

router.use((req, res, next) => {
	if (req.path.includes(MFE_SUFFIX.ASSETS)) {
		return next();
	}
	return roleMiddleware(req, res, next);
});

router.get(/^\/api\/page-config\/(.*)$/, async (req, res) => {
	try {
		let path = req.params[0] || '';
		if (!path || path === '') {
			path = PATHS.HOME;
		}

		const normalizedPath = path.startsWith('/') ? path : `/${path}`;
		const accessToken = req.headers.authorization?.replace('Bearer ', '');
		const { allowed, userRole } = await checkRoleAccess(
			normalizedPath,
			accessToken
		);

		if (!allowed) {
			return res.status(403).json({
				error: 'Доступ запрещен',
				redirect: PATHS.HOME,
				path: normalizedPath,
				userRole: userRole
			});
		}

		const config = getConfigs(normalizedPath);
		const endpoint = endpointsConfig.endpoints.find(e => e.path === normalizedPath);

		if (!endpoint) {
			return res.status(404).json({
				error: `Не существует эндпоинта по пути "${normalizedPath}"`
			});
		}

		res.json({
			path: normalizedPath,
			title: config.title,
			microfrontends: endpoint.microfrontends,
			rootDivs: config.rootDivs
		});
	} catch (error: any) {
		res.status(404).json({ error: error.message });
	}
});

router.get('/api/microfrontends-config', (_req, res) => {
	res.json(microfrontendsConfig);
});

router.get('/', (_req, res) => {
	const html = renderHTML(PATHS.HOME);
	res.setHeader('Content-Type', 'text/html');
	res.send(html);
});

router.get(/.*/, (req, res) => {
	if (!req.path.startsWith('/api') && !req.path.includes('.')) {
		const endpoint = endpointsConfig.endpoints.find(e => e.path === req.path);
		const html = endpoint ? renderHTML(req.path) : renderHTML(PATHS.NOT_FOUND);
		res.setHeader('Content-Type', 'text/html');
		res.status(endpoint ? 200 : 404).send(html);
	} else {
		res.status(404).send('Не найденно');
	}
});

module.exports = router;