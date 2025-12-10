import './index.css';
import { Theme, presetGpnDefault } from '@consta/uikit/Theme';
import { Provider } from 'react-redux';
import { store } from './store/store';

import { RegisterPage } from '@pages/RegisterPage';
import { LoginPage } from '@pages/LoginPage';
import { useEffect, useState } from 'react';
import { RecoveryPage } from '@pages/RecoveryPage';

function App() {
	const [currentPath, setCurrentPath] = useState(window.location.pathname);

	useEffect(() => {
		const handlePopState = () => {
			setCurrentPath(window.location.pathname);
		};

		const handleLocationChange = (event: Event) => {
			const customEvent = event as CustomEvent<{ path: string }>;
			setCurrentPath(
				customEvent.detail?.path || window.location.pathname
			);
		};
		window.addEventListener('popstate', handlePopState);
		window.addEventListener('locationchange', handleLocationChange);
		const interval = setInterval(() => {
			if (window.location.pathname !== currentPath) {
				setCurrentPath(window.location.pathname);
			}
		}, 100);

		return () => {
			window.removeEventListener('popstate', handlePopState);
			window.removeEventListener('locationchange', handleLocationChange);
			clearInterval(interval);
		};
	}, [currentPath]);

	const getCurrentPage = () => {
		if (currentPath === '/auth/register') {
			return <RegisterPage />;
		}
		if (currentPath === '/auth/login') {
			return <LoginPage />;
		}
		if (currentPath === '/auth/forgot-password') {
			return <RecoveryPage />;
		}
	};

	return (
		<Theme
			style={{ height: '100%', width: '100%' }}
			preset={presetGpnDefault}
		>
			<Provider store={store}>{getCurrentPage()}</Provider>
		</Theme>
	);
}

export default App;
