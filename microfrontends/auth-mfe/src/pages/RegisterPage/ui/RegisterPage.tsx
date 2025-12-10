import { useEffect } from 'react';
import { RegisterForm } from '@pages/RegisterPage/features/register';
import { useAppDispatch } from '@shared/hooks/store.hooks';
import { registerFailure } from '../features/register/model/registerSlice';

const RegisterPage = () => {
	const dispatch = useAppDispatch();

	useEffect(() => {
			dispatch(registerFailure({}));
	}, [dispatch]);

	return (
		<main style={{ display: "flex", alignItems: "center", justifyContent: "center", height: "100%"}}>
			<RegisterForm />
		</main>
	);
};

export default RegisterPage;
