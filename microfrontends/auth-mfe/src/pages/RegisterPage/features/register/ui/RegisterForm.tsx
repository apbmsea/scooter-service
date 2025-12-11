import React, { useState } from 'react';
import { useAppDispatch, useAppSelector } from '@shared/hooks/store.hooks';
import {
	clearFieldError,
	registerRequest
} from '@pages/RegisterPage/features/register/model/registerSlice';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';
import { useMaskito } from '@maskito/react';
import { maskitoPhoneOptions } from '@shared/utils/maskPhoneOptions';

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

	const phoneMask = useMaskito({ options: maskitoPhoneOptions });

	const handleChange =
		(field: keyof typeof form) => (value: string | null) => {
			const newValue = value ?? '';
			setForm(prev => ({ ...prev, [field]: newValue }));

			if (errors[field]) {
				dispatch(clearFieldError(field));
			}
		};

	const normalizePhone = (maskedPhone: string) => {
		const digits = maskedPhone.replace(/\D/g, '');
		return '+' + digits;
	};

	const handleSubmit = (e: React.FormEvent) => {
		e.preventDefault();

		const payload = {
			...form,
			phone: normalizePhone(form.phone)
		};

		dispatch(registerRequest(payload));
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
			<h2>Регистрация</h2>

			<TextField
				label='Почта'
				required
				withClearButton
				placeholder='example@gmail.com'
				value={form.email}
				status={errors.email ? 'alert' : undefined}
				caption={errors.email || ''}
				onChange={handleChange('email')}
				disabled={isLoading}
			/>

			<TextField
				label='Имя'
				required
				withClearButton
				placeholder='Иван'
				value={form.firstName}
				status={errors.firstName ? 'alert' : undefined}
				caption={errors.firstName || ''}
				onChange={handleChange('firstName')}
				disabled={isLoading}
			/>

			<TextField
				label='Фамилия'
				required
				withClearButton
				placeholder='Иванов'
				value={form.lastName}
				status={errors.lastName ? 'alert' : undefined}
				caption={errors.lastName || ''}
				onChange={handleChange('lastName')}
				disabled={isLoading}
			/>

			<TextField
				label='Телефон'
				required
				placeholder='+7 (999) 999-99-99'
				inputRef={phoneMask}
				value={form.phone}
				status={errors.phone ? 'alert' : undefined}
				caption={errors.phone || ''}
				onChange={handleChange('phone')}
				disabled={isLoading}
			/>

			<TextField
				type='password'
				label='Пароль'
				required
				withClearButton
				placeholder='********'
				value={form.password}
				status={errors.password ? 'alert' : undefined}
				caption={errors.password || ''}
				onChange={handleChange('password')}
				disabled={isLoading}
			/>

			<Button loading={isLoading} type='submit' label='Продолжить' />

			<a
				style={{
					fontSize: '.8rem',
					textAlign: 'center',
					color: 'rgba(0, 173, 253, 1)',
					cursor: 'pointer'
				}}
				href='/auth/login'
			>
				Уже есть аккаунт? Вход
			</a>
		</form>
	);
};

export default RegisterForm;
