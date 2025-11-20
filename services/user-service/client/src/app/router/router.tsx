import { createBrowserRouter } from 'react-router-dom';
import Layout from '@app/Layout';
import { HomePage } from '@pages/HomePage';
import { RegisterPage } from '@pages/RegisterPage';
import { LoginPage } from '@pages/LoginPage';
import { RecoveryPage } from '@pages/RecoveryPage';
import { UserPage } from '@pages/UserPage';

export const router = createBrowserRouter([
	{
		path: '/',
		element: <Layout />,
		children: [
			{ path: '/home', index: true, element: <HomePage /> },
			{ path: '/auth/register', element: <RegisterPage /> },
			{ path: '/auth/login', element: <LoginPage /> },
			{ path: '/auth/forgot-password', element: <RecoveryPage /> },
			{ path: '/users/me', element: <UserPage /> }
		]
	}
]);
