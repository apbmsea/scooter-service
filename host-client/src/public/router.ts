interface PageConfig {
	title: string;
	microfrontends: string[];
	rootDivs: string;
}

class ClientRouter {
	private currentPath: string;

	constructor() {
		this.currentPath = window.location.pathname;
		this.init();
	}

	private init(): void {
		document.addEventListener('click', e => {
			const link = (e.target as Element).closest('a[href]') as HTMLAnchorElement;
			if (link && this.isInternalLink(link.href)) {
				e.preventDefault();
				this.navigate(new URL(link.href).pathname);
			}
		});

		window.addEventListener('popstate', async e => {
			const path = e.state ? e.state.path : window.location.pathname;
			this.currentPath = path;
			
			if (e.state) {
				try {
					const normalizedPath = path === '/' ? '/home' : path;
					const accessToken = localStorage.getItem('accessToken');
					const headers: Record<string, string> = { 'Content-Type': 'application/json' };
					if (accessToken) {
						headers['Authorization'] = `Bearer ${accessToken}`;
					}
					const response = await fetch(`/api/page-config${normalizedPath}`, {
						credentials: 'include',
						headers
					});
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

	private async waitFor(condition: () => boolean, interval = 50): Promise<void> {
		return new Promise(resolve => {
			const checkInterval = setInterval(() => {
				if (condition()) {
					clearInterval(checkInterval);
					resolve();
				}
			}, interval);
		});
	}

	private async registerInitialMicrofrontends(): Promise<void> {
		if (!(window as any).mfeManager) {
			await this.waitFor(() => !!(window as any).mfeManager);
		}

		if (!(window as any).mfeManager.mfeConfigs) {
			await this.waitFor(() => !!(window as any).mfeManager.mfeConfigs);
		}

		const normalizedPath = window.location.pathname === '/' ? '/home' : window.location.pathname;
		const accessToken = localStorage.getItem('accessToken');
		const headers: Record<string, string> = { 'Content-Type': 'application/json' };
		if (accessToken) {
			headers['Authorization'] = `Bearer ${accessToken}`;
		}
		const checkResponse = await fetch(`/api/page-config${normalizedPath}`, {
			credentials: 'include',
			headers
		});

		if (checkResponse.status === 403) {
			const errorData = await checkResponse.json();
			const redirectPath = errorData.redirect || '/home';
			window.location.href = redirectPath;
			return;
		}

		const rootElements = document.querySelectorAll('[id$="-mfe-root"]');
		rootElements.forEach(root => {
			const mfeName = root.id.replace('-mfe-root', '');
			if (!(window as any).mfeManager.loadedMFEs.has(mfeName)) {
				const mfeConfig = (window as any).mfeManager.mfeConfigs?.microfrontends?.find(
					(m: any) => m.name === mfeName
				);
				if (mfeConfig) {
					(window as any).mfeManager.loadedMFEs.set(mfeName, {
						config: mfeConfig,
						rootId: root.id,
						loaded: true,
						mounted: true
					});
				}
			}
		});
	}

	private isInternalLink(href: string): boolean {
		try {
			const url = new URL(href, window.location.origin);
			return url.origin === window.location.origin && !href.startsWith('#');
		} catch {
			return false;
		}
	}

	navigate(path: string): void {
		if (this.currentPath !== path) {
			window.history.pushState({ path }, '', path);
			this.currentPath = path;
			this.loadPage(path, true);
		}
	}

	private async loadPage(path: string, shouldUpdateHistory = true): Promise<void> {
		try {
			const normalizedPath = path === '/' ? '/home' : path;
			const accessToken = localStorage.getItem('accessToken');
			const headers: Record<string, string> = { 'Content-Type': 'application/json' };
			if (accessToken) {
				headers['Authorization'] = `Bearer ${accessToken}`;
			}
			const response = await fetch(`/api/page-config${normalizedPath}`, {
				credentials: 'include',
				headers
			});

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

			const config: PageConfig = await response.json();
			document.title = config.title;

			if ((window as any).mfeManager) {
				await (window as any).mfeManager.updateMicrofrontends(config);
			}
		} catch (error) {
			console.error('Ошибка загрузки страницы:', error);
			this.load404Page();
		}
	}

	private async load404Page(): Promise<void> {
		try {
			const error404Response = await fetch(`/api/page-config/404`);
			if (error404Response.ok) {
				const error404Config: PageConfig = await error404Response.json();
				document.title = error404Config.title;
				if ((window as any).mfeManager) {
					await (window as any).mfeManager.updateMicrofrontends(error404Config);
				}
			}
		} catch (err) {
			console.error('Ошибка загрузки 404:', err);
		}
	}
}

function initRouter(): void {
	const router = new ClientRouter();
	(window as any).clientRouter = router;
	(window as any).__MF_ROUTER__ = {
		goTo: (pathname: string) => router.navigate(pathname)
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