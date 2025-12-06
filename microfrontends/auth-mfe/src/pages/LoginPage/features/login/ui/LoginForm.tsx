import React, { useEffect, useState } from 'react';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';
import { clearFieldError, loginRequest } from '../model/loginSlice';
import { useAppDispatch, useAppSelector } from '@shared/hooks/store.hooks';

const LoginForm = () => {
	const dispatch = useAppDispatch();
	const { isLoading, errors } = useAppSelector(state => state.login);

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
		const savedEmail = localStorage.getItem('email');
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
			<h2>Вход в аккаунт</h2>

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

			<a
				style={{
					fontSize: '.8rem',
					color: 'rgba(0, 173, 253, 1)',
					cursor: 'pointer'
				}}
				href='/auth/forgot-password'
			>
				Забыли пароль?
			</a>

			<Button loading={isLoading} type='submit' label='Войти' />

			<a
				style={{
					fontSize: '.8rem',
					textAlign: 'center',
					color: 'rgba(0, 173, 253, 1)',
					cursor: 'pointer'
				}}
				href='/auth/register'
			>
				Нет аккаунта? Регистрация
			</a>
		</form>
	);
};

export default LoginForm;
