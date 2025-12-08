import { useEffect } from 'react';
import { LoginForm } from '@pages/LoginPage/features/login';
import { useAppDispatch } from '@shared/hooks/store.hooks';
import { loginFailure } from '../features/login/model/loginSlice';

const LoginPage = () => {
	const dispatch = useAppDispatch();

	useEffect(() => {
		dispatch(loginFailure({}));
	}, [dispatch]);

	return (
		<main
			style={{
				display: 'flex',
				alignItems: 'center',
				justifyContent: 'center',
				height: '100%'
			}}
		>
			<LoginForm />
		</main>
	);
};

export default LoginPage;
