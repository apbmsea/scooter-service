import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';
import type { RootState } from '@shared/types/store.types';
import { clearFieldError, loginRequest } from '../model/loginSlice';
import { local } from '@shared/utils/localStorageHelper';

const LoginForm = () => {
	const dispatch = useDispatch();
	const { isLoading, errors } = useSelector(
		(state: RootState) => state.login
	);

	const [form, setForm] = useState({
		email: '',
		password: ''
	});

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
		dispatch(loginRequest(form));
	};

	useEffect(() => {
		const savedEmail = local.get('email');
		if (savedEmail) {
			// eslint-disable-next-line react-hooks/set-state-in-effect
			setForm({
				email: String(savedEmail),
				password: ''
			});
		}
	}, []);

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
				label='Пароль'
				placeholder='********'
				required
				withClearButton
				value={form.password}
				status={errors.password ? 'alert' : undefined}
				caption={errors.password || ''}
				onChange={handleChange('password')}
				disabled={isLoading}
			/>

			<Button loading={isLoading} type='submit' label='Войти' />
		</form>
	);
};

export default LoginForm;
