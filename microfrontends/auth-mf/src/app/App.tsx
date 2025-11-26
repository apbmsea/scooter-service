import { Route, Routes, useNavigate } from 'react-router-dom';
import { Theme, presetGpnDefault } from '@consta/uikit/Theme';
import { Provider } from 'react-redux';
import { store } from './store/store';

import { RegisterPage } from '@pages/RegisterPage';
import { LoginPage } from '@pages/LoginPage';
import { RecoveryPage } from '@pages/RecoveryPage';
import { useEffect } from 'react';
import { setNavigate } from '@shared/utils/navigate';

function App() {
	const navigate = useNavigate();

	useEffect(() => {
		setNavigate(navigate);
	}, [navigate]);

	return (
		<Theme
			style={{ height: '100%', width: '100%' }}
			preset={presetGpnDefault}
		>
			<Provider store={store}>
				<Routes>
					<Route path='/register' element={<RegisterPage />} />
					<Route path='/login' element={<LoginPage />} />
					<Route path='/forgot-password' element={<RecoveryPage />} />
				</Routes>
			</Provider>
		</Theme>
	);
}

export default App;
