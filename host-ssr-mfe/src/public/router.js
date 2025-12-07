class ClientRouter {
	constructor() {
		this.currentPath = window.location.pathname;
		this.init();
	}

	init() {
		document.addEventListener('click', e => {
			const link = e.target.closest('a[href]');
			if (link && this.isInternalLink(link.href)) {
				e.preventDefault();
				this.navigate(link.pathname);
			}
		});

		window.addEventListener('popstate', async e => {
			const path = e.state ? e.state.path : window.location.pathname;
			this.currentPath = path;
			
			if (e.state) {
				try {
					const normalizedPath = path === '/' ? '/home' : path;
					const response = await fetch(`/api/page-config${normalizedPath}`);
					this.loadPage(response.ok ? path : '/404', false);
				} catch (error) {
					console.error('Ошибка при popstate:', error);
					this.loadPage('/404', false);
				}
			} else {
				this.loadPage(path, false);
			}
		});

		this.registerInitialMicrofrontends().catch(err => {
			console.error('Ошибка регистрации микрофронтендов при инициализации', err);
		});
	}

	async waitFor(condition, interval = 50) {
		return new Promise(resolve => {
			const checkInterval = setInterval(() => {
				if (condition()) {
					clearInterval(checkInterval);
					resolve();
				}
			}, interval);
		});
	}

	async registerInitialMicrofrontends() {
		if (!window.mfeManager) {
			await this.waitFor(() => window.mfeManager);
		}

		if (!window.mfeManager.mfeConfigs) {
			await this.waitFor(() => window.mfeManager.mfeConfigs);
		}

		const rootElements = document.querySelectorAll('[id$="-mfe-root"]');
		rootElements.forEach(root => {
			const mfeName = root.id.replace('-mfe-root', '');
			if (!window.mfeManager.loadedMFEs.has(mfeName)) {
				const mfeConfig = window.mfeManager.mfeConfigs?.microfrontends?.find(
					m => m.name === mfeName
				);
				if (mfeConfig) {
					window.mfeManager.loadedMFEs.set(mfeName, {
						config: mfeConfig,
						rootId: root.id,
						loaded: true,
						mounted: true
					});
				}
			}
		});
	}

	isInternalLink(href) {
		try {
			const url = new URL(href, window.location.origin);
			return url.origin === window.location.origin && !href.startsWith('#');
		} catch {
			return false;
		}
	}

	navigate(path) {
		if (this.currentPath !== path) {
			window.history.pushState({ path }, '', path);
			this.currentPath = path;
			this.loadPage(path, true);
		}
	}

	async loadPage(path, shouldUpdateHistory = true) {
		try {
			const normalizedPath = path === '/' ? '/home' : path;
			const response = await fetch(`/api/page-config${normalizedPath}`);

			if (response.status === 403) {
				const errorData = await response.json();
				console.warn(`Доступ запрещен: путь=${errorData.path}, роль=${errorData.userRole}`);
				const redirectPath = errorData.redirect || '/home';
				if (shouldUpdateHistory) {
					window.history.pushState({ path: redirectPath }, '', redirectPath);
					this.currentPath = redirectPath;
				}
				return this.loadPage(redirectPath, false);
			}

			if (!response.ok) {
				return this.load404Page();
			}

			const config = await response.json();
			document.title = config.title;

			if (window.mfeManager) {
				await window.mfeManager.updateMicrofrontends(config);
			}
		} catch (error) {
			console.error('Ошибка загрузки страницы:', error);
			this.load404Page();
		}
	}

	async load404Page() {
		try {
			const error404Response = await fetch(`/api/page-config/404`);
			if (error404Response.ok) {
				const error404Config = await error404Response.json();
				document.title = error404Config.title;
				if (window.mfeManager) {
					await window.mfeManager.updateMicrofrontends(error404Config);
				}
			}
		} catch (err) {
			console.error('Ошибка загрузки 404:', err);
		}
	}
}

function initRouter() {
	const router = new ClientRouter();
	window.clientRouter = router;
	window.__MF_ROUTER__ = {
		goTo: pathname => router.navigate(pathname)
	};
}

if (typeof window !== 'undefined') {
	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', () => {
			setTimeout(initRouter, 100);
		});
	} else {
		setTimeout(initRouter, 100);
	}
}
