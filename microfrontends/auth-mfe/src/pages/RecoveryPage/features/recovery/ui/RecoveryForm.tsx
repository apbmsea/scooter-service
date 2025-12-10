import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '@shared/hooks/store.hooks';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';
import { clearFieldError, recoveryRequest } from '../model/recoverySlice';

const RecoveryForm = () => {
	const dispatch = useAppDispatch();
	const { isLoading, errors } = useAppSelector(state => state.recovery);

	const [form, setForm] = useState({
		email: '',
		newPassword: ''
	});

	useEffect(() => {
		const savedEmail = localStorage.getItem('email');
		if (savedEmail) {
			// eslint-disable-next-line react-hooks/set-state-in-effect
			setForm({
				email: String(savedEmail),
				newPassword: ''
			});
		}
	}, []);

	const handleChange =
		(field: keyof typeof form) => (value: string | null) => {
			const newValue = value ?? '';
			setForm(prev => ({ ...prev, [field]: newValue }));

			if (errors[field]) {
				dispatch(clearFieldError(field));
			}
		};

	const handleSubmit = (e: React.FormEvent) => {
		e.preventDefault();
		dispatch(recoveryRequest(form));
	};

	return (
		<form
			style={{
				display: 'flex',
				flexDirection: 'column',
				gap: 15,
				width: 350
			}}
			onSubmit={handleSubmit}
		>
			<h2>Восстановление аккаунта</h2>

			<TextField
				label='Почта'
				placeholder='example@gmail.com'
				required
				withClearButton
				value={form.email}
				status={errors.email ? 'alert' : undefined}
				caption={errors.email || ''}
				onChange={handleChange('email')}
				disabled={isLoading}
			/>

			<TextField
				type='password'
				label='Новый пароль'
				placeholder='********'
				required
				withClearButton
				value={form.newPassword}
				status={errors.newPassword ? 'alert' : undefined}
				caption={errors.newPassword || ''}
				onChange={handleChange('newPassword')}
				disabled={isLoading}
			/>

			<Button loading={isLoading} type='submit' label='Воcстановить' />
		</form>
	);
};

export default RecoveryForm;
