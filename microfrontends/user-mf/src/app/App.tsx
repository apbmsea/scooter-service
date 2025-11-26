import { Route, Routes, useNavigate } from 'react-router-dom';
import { Theme, presetGpnDefault } from '@consta/uikit/Theme';
import { Provider } from 'react-redux';
import { store } from './store/store';

import { UserPage } from '@pages/UserPage';
import { useEffect } from 'react';
import { setNavigate } from '@shared/utils/navigate';

function App() {
	const navigate = useNavigate();

	useEffect(() => {
		setNavigate(navigate);
	}, [navigate]);

	return (
		<Theme style={{ height: '100%', width: '100%' }} preset={presetGpnDefault}>
			<Provider store={store}>
				<Routes>
					<Route path='/me' element={<UserPage />} />
				</Routes>
			</Provider>
		</Theme>
	);
}

export default App;
