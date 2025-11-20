import { setNavigate } from '@shared/utils/navigate';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Theme, presetGpnDefault } from '@consta/uikit/Theme';
import { useDispatch } from 'react-redux';
import { refreshRequest } from '@features/refresh/model/refreshSlice';

const Layout = () => {
	const dispatch = useDispatch();
	const navigate = useNavigate();

	useEffect(() => {
		setNavigate(navigate);
	}, [navigate]);

	useEffect(() => {
		const refreshToken = () => {
			const token = localStorage.getItem('accessToken');

			if (token) {
				dispatch(refreshRequest());
			}
		};

		refreshToken();
	}, [dispatch]);

	return (
		<Theme preset={presetGpnDefault}>
			<div className='app-layout'>
				<Outlet />
			</div>
		</Theme>
	);
};

export default Layout;
