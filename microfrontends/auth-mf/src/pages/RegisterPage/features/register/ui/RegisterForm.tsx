import React, { useState } from 'react';
import { useAppDispatch, useAppSelector } from '@shared/hooks/store.hooks';
import { clearFieldError, registerRequest } from '@pages/RegisterPage/features/register/model/registerSlice';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';

const RegisterForm = () => {
	const dispatch = useAppDispatch();
	const { isLoading, errors } = useAppSelector(state => state.register);

	const [form, setForm] = useState({
		email: '',
		firstName: '',
		lastName: '',
		phone: '',
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
		dispatch(registerRequest(form));
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
				label='Имя'
				placeholder='Иван'
				required
				withClearButton
				value={form.firstName}
				status={errors.firstName ? 'alert' : undefined}
				caption={errors.firstName || ''}
				onChange={handleChange('firstName')}
				disabled={isLoading}
			/>

			<TextField
				label='Фамилия'
				placeholder='Иванов'
				required
				withClearButton
				value={form.lastName}
				status={errors.lastName ? 'alert' : undefined}
				caption={errors.lastName || ''}
				onChange={handleChange('lastName')}
				disabled={isLoading}
			/>

			<TextField
				label='Телефон'
				placeholder='Введите номер'
				required
				withClearButton
				value={form.phone}
				status={errors.phone ? 'alert' : undefined}
				caption={errors.phone || ''}
				onChange={handleChange('phone')}
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

			<Button loading={isLoading} type='submit' label='Продолжить' />
		</form>
	);
};

export default RegisterForm;
