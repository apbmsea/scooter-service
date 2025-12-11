import { StrictMode } from 'react';
import { createRoot, type Root } from 'react-dom/client';
import App from './App';

let root: Root | null = null;

const renderApp = (container: Element) => {
	if (!root) {
		root = createRoot(container);
	}
	root.render(
		<StrictMode>
			<App />
		</StrictMode>
	);
};

export function mount() {
	const container =
		document.getElementById('auth-mfe-root') ??
		document.getElementById('root');
	if (!container) {
		return;
	}
	renderApp(container);
}

export function resetRoot() {
	if (root) {
		try {
			root.unmount();
		} catch (error) {
			console.error('Auth: Unmount error', error);
		} finally {
			root = null;
		}
	}
}

if (document.getElementById('root')) mount();

declare global {
	interface Window {
		__AUTH_MFE_MOUNT__?: () => void;
		__AUTH_MFE_RESET__?: () => void;
	}
}

if (typeof window !== 'undefined') {
	window.__AUTH_MFE_MOUNT__ = mount;
	window.__AUTH_MFE_RESET__ = resetRoot;
}
