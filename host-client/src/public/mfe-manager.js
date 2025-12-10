class MicrofrontendManager {
	constructor() {
		this.loadedMFEs = new Map();
		this.mfeConfigs = null;
		this.init();
	}

	async init() {
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

	async preloadAllMicrofrontends() {
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

	async updateMicrofrontends(pageConfig) {
		const requiredMFEs = pageConfig.microfrontends || [];
		const currentlyMounted = Array.from(this.loadedMFEs.entries())
			.filter(([_, mfe]) => mfe.mounted)
			.map(([name, _]) => name);

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

	updateRootDivs(rootDivsHTML) {
		const app = document.getElementById('app');
		if (!app) return;

		const appContent = document.getElementById('app-content');
		if (appContent) {
			appContent.remove();
		}

		const tempDiv = document.createElement('div');
		tempDiv.innerHTML = rootDivsHTML;

		const newRoots = Array.from(tempDiv.children);
		const requiredIds = newRoots.map(root => root.id);

		if (newRoots.length === 1 && newRoots[0].id === 'app-content') {
			const clonedContent = newRoots[0].cloneNode(true);
			app.appendChild(clonedContent);

			const allRoots = app.querySelectorAll('[id$="-mfe-root"]');
			allRoots.forEach(root => {
				root.style.display = 'none';
			});
			return;
		}

		newRoots.forEach(newRoot => {
			const existingRoot = document.getElementById(newRoot.id);
			if (!existingRoot) {
				const clonedRoot = newRoot.cloneNode(true);
				app.appendChild(clonedRoot);
			}
		});

		const allRoots = app.querySelectorAll('[id$="-mfe-root"]');
		allRoots.forEach(root => {
			if (requiredIds.includes(root.id)) {
				root.style.display = '';
			} else {
				root.style.display = 'none';
			}
		});
	}

	async loadMicrofrontend(name) {
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

	async loadCSS(href, name) {
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
				reject(new Error(`Failed to load CSS for ${name}`));
			document.head.appendChild(link);
		});
	}

	async loadJS(src, name) {
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
				reject(new Error(`Failed to load JS for ${name}`));
			document.body.appendChild(script);
		});
	}

	async mountMicrofrontend(name) {
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
		const mountFunction = window[mountFunctionName];

		if (mountFunction && typeof mountFunction === 'function') {
			try {
				if (mfe.mounted) {
					const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
					const unmountFunction = window[unmountFunctionName];
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
				const fn = window[mountFunctionName];
				if (fn && typeof fn === 'function') {
					clearInterval(checkInterval);
					try {
						if (mfe.mounted) {
							const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
							const unmountFunction = window[unmountFunctionName];
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

	async unmountMicrofrontend(name, removeFromDOM = true) {
		const mfe = this.loadedMFEs.get(name);
		if (!mfe) {
			return;
		}
		const unmountFunctionName = `__${name.toUpperCase()}_MFE_UNMOUNT__`;
		const unmountFunction = window[unmountFunctionName];

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

	getMfeConfig(name) {
		if (!this.mfeConfigs) {
			return null;
		}
		return this.mfeConfigs.microfrontends.find(m => m.name === name);
	}
}

if (typeof window !== 'undefined') {
	window.mfeManager = new MicrofrontendManager();
}
