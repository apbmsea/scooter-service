import { setNavigate } from '@shared/utils/navigate';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Theme, presetGpnDefault } from '@consta/uikit/Theme';

const Layout = () => {
	const navigate = useNavigate();

	useEffect(() => {
		setNavigate(navigate);
	}, [navigate]);

	return (
		<Theme preset={presetGpnDefault}>
			<div className='app-layout'>
				<Outlet />
			</div>
		</Theme>
	);
};

export default Layout;
