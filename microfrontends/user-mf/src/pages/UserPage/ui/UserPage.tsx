import { UserForm } from '../features/user';

const UserPage = () => {
	return (
		<main
			style={{
				display: 'flex',
				alignItems: 'center',
				justifyContent: 'center',
				height: '100%'
			}}
		>
			<UserForm />
		</main>
	);
};

export default UserPage;
