import { RegisterForm } from '@features/auth/register';

const RegisterPage = () => {
	return (
		<main style={{ display: "flex", alignItems: "center", justifyContent: "center", height: "100vh"}}>
			<RegisterForm />
		</main>
	);
};

export default RegisterPage;
