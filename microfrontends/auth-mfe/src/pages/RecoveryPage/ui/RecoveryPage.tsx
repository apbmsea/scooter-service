import { useEffect } from 'react';
import { RecoveryForm } from '@pages/RecoveryPage/features/recovery';
import { useAppDispatch } from '@shared/hooks/store.hooks';
import { recoveryFailure } from '../features/recovery/model/recoverySlice';

const RecoveryPage = () => {
	const dispatch = useAppDispatch();

	useEffect(() => {
		dispatch(recoveryFailure({}));
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
			<RecoveryForm />
		</main>
	);
};

export default RecoveryPage;
