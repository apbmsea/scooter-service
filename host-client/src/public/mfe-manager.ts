interface MicrofrontendConfig {
	name: string;
	path: string;
}

interface MfeConfigs {
	microfrontends: MicrofrontendConfig[];
}

interface PageConfig {
	microfrontends: string[];
	rootDivs: string;
}

interface LoadedMFE {
	config: MicrofrontendConfig;
	rootId: string;
	loaded: boolean;
	mounted?: boolean;
}

class MicrofrontendManager {
	private loadedMFEs = new Map<string, LoadedMFE>();
	private mfeConfigs: MfeConfigs | null = null;

	constructor() {
		this.init();
	}

	async init(): Promise<void> {
		const response = await fetch('/api/microfrontends-config');
		if (response.ok) {
			this.mfeConfigs = await response.json();

			this.preloadAllMicrofrontends().catch(err => {
				console.warn(
					'Ошибка предзагрузки микрофронтендов при инициализации',
					err
				);
			});
		}
	}

	async preloadAllMicrofrontends(): Promise<void> {
		if (!this.mfeConfigs?.microfrontends) {
			return;
		}

		const names = this.mfeConfigs.microfrontends.map(m => m.name);

		for (const name of names) {
			if (this.loadedMFEs.has(name)) continue;

			try {
				await this.loadMicrofrontend(name);
			} catch (error) {
				console.warn(`Не удалось предзагрузить MFE ${name}:`, error);
			}
		}
	}

	async updateMicrofrontends(pageConfig: PageConfig): Promise<void> {
		const requiredMFEs = pageConfig.microfrontends || [];
		const currentlyMounted = Array.from(this.loadedMFEs.entries())
			.filter(([, mfe]) => mfe.mounted)
			.map(([name]) => name);

		const toLoad = requiredMFEs.filter(mfe => !this.loadedMFEs.has(mfe));
		const toUnmount = currentlyMounted.filter(
			mfe => !requiredMFEs.includes(mfe)
		);
		const toMount = requiredMFEs.filter(
			mfe => !currentlyMounted.includes(mfe)
		);

		for (const mfeName of toUnmount) {
			await this.unmountMicrofrontend(mfeName, false);
		}

		for (const mfeName of toLoad) {
			await this.loadMicrofrontend(mfeName);
		}

		this.updateRootDivs(pageConfig.rootDivs);

		for (const mfeName of toMount) {
			await this.mountMicrofrontend(mfeName);
		}
	}

	private updateRootDivs(rootDivsHTML: string): void {
		const app = document.getElementById('app');
		if (!app) return;

		const appContent = document.getElementById('app-content');
		if (appContent) {
			appContent.remove();
		}

		const tempDiv = document.createElement('div');
		tempDiv.innerHTML = rootDivsHTML;

		const newRoots = Array.from(tempDiv.children) as HTMLElement[];
		const requiredIds = newRoots.map(root => root.id);

		if (newRoots.length === 1 && newRoots[0].id === 'app-content') {
			const clonedContent = newRoots[0].cloneNode(true) as HTMLElement;
			app.appendChild(clonedContent);

			const allRoots = app.querySelectorAll('[id$="-mfe-root"]');
			allRoots.forEach(root => {
				(root as HTMLElement).style.display = 'none';
			});
			return;
		}

		newRoots.forEach(newRoot => {
			const existingRoot = document.getElementById(newRoot.id);
			if (!existingRoot) {
				const clonedRoot = newRoot.cloneNode(true) as HTMLElement;
				app.appendChild(clonedRoot);
			}
		});

		const allRoots = app.querySelectorAll('[id$="-mfe-root"]');
		allRoots.forEach(root => {
			if (requiredIds.includes(root.id)) {
				(root as HTMLElement).style.display = '';
			} else {
				(root as HTMLElement).style.display = 'none';
			}
		});
	}

	async loadMicrofrontend(name: string): Promise<void> {
		if (this.loadedMFEs.has(name)) {
			return;
		}

		const mfeConfig = this.getMfeConfig(name);
		if (!mfeConfig) {
			throw new Error(`Микрофронтенд ${name} не найден в конфиге`);
		}

		await this.loadCSS(`${mfeConfig.path}/assets/main.css`, name);
		await this.loadJS(`${mfeConfig.path}/assets/main.js`, name);

		this.loadedMFEs.set(name, {
			config: mfeConfig,
			rootId: `${name}-mfe-root`,
			loaded: true
		});
	}

	private async loadCSS(href: string, name: string): Promise<void> {
		return new Promise((resolve, reject) => {
			const existingLink = document.querySelector(
				`link[data-mfe="${name}"]`
			);
			if (existingLink) {
				resolve();
				return;
			}

			const link = document.createElement('link');
			link.rel = 'stylesheet';
			link.href = href;
			link.setAttribute('data-mfe', name);
			link.onload = () => resolve();
			link.onerror = () =>
				reject(new Error(`Ошибка загрузки CSS ${name}`));
			document.head.appendChild(link);
		});
	}

	private async loadJS(src: string, name: string): Promise<void> {
		return new Promise((resolve, reject) => {
			const existingScript = document.querySelector(
				`script[data-mfe="${name}"]`
			);
			if (existingScript) {
				resolve();
				return;
			}

			const script = document.createElement('script');
			script.type = 'module';
			script.src = src;
			script.setAttribute('data-mfe', name);
			script.onload = () => resolve();
			script.onerror = () =>
				reject(new Error(`Ошибка загрузки JS ${name}`));
			document.body.appendChild(script);
		});
	}

	async mountMicrofrontend(name: string): Promise<void> {
		const mfe = this.loadedMFEs.get(name);
		if (!mfe || !mfe.loaded) {
			return;
		}

		const rootId = mfe.rootId;
		const rootElement = document.getElementById(rootId);
		if (!rootElement) {
			console.warn(`Не найден рутовый элемент ${rootId} для ${name}`);
			return;
		}

		rootElement.style.display = '';

		const mountFunctionName = `__${name.toUpperCase()}_MFE_MOUNT__`;
		const mountFunction = (window as any)[mountFunctionName];

		if (mountFunction && typeof mountFunction === 'function') {
			try {
				if (mfe.mounted) {
					const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
					const unmountFunction = (window as any)[unmountFunctionName];
					if (
						unmountFunction &&
						typeof unmountFunction === 'function'
					) {
						unmountFunction();
					}
				}

				mountFunction(rootId);
				mfe.mounted = true;
			} catch (error) {
				console.error(`Ошибка монтирования ${name}:`, error);
			}
		} else {
			let attempts = 0;
			const maxAttempts = 50;
			const checkInterval = setInterval(() => {
				attempts++;
				const fn = (window as any)[mountFunctionName];
				if (fn && typeof fn === 'function') {
					clearInterval(checkInterval);
					try {
						if (mfe.mounted) {
							const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
							const unmountFunction = (window as any)[unmountFunctionName];
							if (
								unmountFunction &&
								typeof unmountFunction === 'function'
							) {
								unmountFunction();
							}
						}
						fn(rootId);
						mfe.mounted = true;
					} catch (error) {
						console.error(`Ошибка монтирования ${name}:`, error);
					}
				} else if (attempts >= maxAttempts) {
					clearInterval(checkInterval);
					console.warn(
						`Функция монитрования ${mountFunctionName} не найденна для ${name} после ${maxAttempts} попыток`
					);
				}
			}, 100);
		}
	}

	async unmountMicrofrontend(name: string, removeFromDOM = true): Promise<void> {
		const mfe = this.loadedMFEs.get(name);
		if (!mfe) {
			return;
		}
		const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
		const unmountFunction = (window as any)[unmountFunctionName];

		if (unmountFunction && typeof unmountFunction === 'function') {
			try {
				unmountFunction();
			} catch (error) {
				console.error(`Ошибка размонтирования ${name}:`, error);
			}
		}

		if (removeFromDOM) {
			const rootElement = document.getElementById(mfe.rootId);
			if (rootElement) {
				rootElement.remove();
			}
		} else {
			const rootElement = document.getElementById(mfe.rootId);
			if (rootElement) {
				rootElement.style.display = 'none';
			}
		}

		mfe.mounted = false;
	}

	private getMfeConfig(name: string): MicrofrontendConfig | null {
		if (!this.mfeConfigs) {
			return null;
		}
		return this.mfeConfigs.microfrontends.find(m => m.name === name) || null;
	}
}

if (typeof window !== 'undefined') {
	(window as any).mfeManager = new MicrofrontendManager();
}