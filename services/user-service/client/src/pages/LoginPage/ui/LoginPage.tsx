import { LoginForm } from '@features/auth/login';

const LoginPage = () => {
	return (
		<main style={{ display: "flex", alignItems: "center", justifyContent: "center", height: "100vh"}}>
			<LoginForm />
		</main>
	);
};

export default LoginPage;
