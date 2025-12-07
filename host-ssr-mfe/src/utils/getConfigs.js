const fs = require('fs');
const path = require('path');
const endpointsConfig = require('../configs/endpoints.config');
const microfrontendsConfig = require('../configs/microfrontends.config');
const envConfig = require('../configs/env.config');
const { MFE_SUFFIX } = require('../constants');

function getAssets(names) {
	const assets = {
		css: [],
		js: [],
		rootDivs: [],
		mountScripts: []
	};

	names.forEach(name => {
		const mfe = microfrontendsConfig.microfrontends.find(
			m => m.name === name
		);
		if (!mfe) throw new Error(`Микрофронтенд ${name} не найден`);

		const rootId = `${name}${MFE_SUFFIX.ROOT}`;
		assets.css.push(
			`<link rel="stylesheet" href="/${name}${MFE_SUFFIX.ASSETS}${envConfig.mfe.assets.path}/${envConfig.mfe.assets.cssFile}">`
		);
		assets.rootDivs.push(`<div id="${rootId}"></div>`);
		assets.js.push(
			`<script type="module" src="/${name}${MFE_SUFFIX.ASSETS}${envConfig.mfe.assets.path}/${envConfig.mfe.assets.jsFile}"></script>`
		);
		assets.mountScripts.push(`
			<script>
 				document.addEventListener('DOMContentLoaded', () => {
    				window.__${name.toUpperCase()}${MFE_SUFFIX.MOUNT} && window.__${name.toUpperCase()}${MFE_SUFFIX.MOUNT}('${rootId}');
  				});
			</script>
    `);
	});

	return assets;
}

function getStaticPageContent(staticPath) {
	const staticFilePath = path.join(__dirname, '../static', `${staticPath}.html`);
	try {
		if (fs.existsSync(staticFilePath)) {
			return fs.readFileSync(staticFilePath, 'utf-8');
		}
	} catch (error) {
		console.error(`Ошибка чтения статической страницы ${staticFilePath}:`, error);
	}
	return null;
}

function getConfigs(routePath) {
	const endpoint = endpointsConfig.endpoints.find(e => e.path === routePath);
	if (!endpoint) throw new Error(`Эндпоинт не найден по пути "${routePath}"`);

	let assets;
	if (endpoint.microfrontends.length > 0) {
		assets = getAssets(endpoint.microfrontends);
	} else {
		const staticPath = routePath.replace(/^\//, '');
		const staticContent = getStaticPageContent(staticPath);
		
		assets = {
			css: [],
			js: [],
			rootDivs: staticContent || [
				'<div id="app-content">Ошибка сервера</div>'
			],
			mountScripts: []
		};
	}

	return {
		title: endpoint.meta.title,
		css: assets.css.join('\n    '),
		js: assets.js.join('\n    '),
		rootDivs: Array.isArray(assets.rootDivs) 
			? assets.rootDivs.join('\n    ') 
			: assets.rootDivs,
		mountScripts: assets.mountScripts.join('\n    ')
	};
}

module.exports = {
	getConfigs
};
