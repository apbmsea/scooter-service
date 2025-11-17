import { setNavigate } from '@shared/utils/navigate';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

const Layout = () => {
	const navigate = useNavigate();

	useEffect(() => {
		setNavigate(navigate);
	}, [navigate]);

	return (
		<div className='app-layout'>
			<Outlet />
		</div>
	);
};

export default Layout;