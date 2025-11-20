import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Button } from '@consta/uikit/Button';
import { TextField } from '@consta/uikit/TextField';
import type { RootState } from '@shared/types/store.types';
import {
	clearFieldError,
	getIUserRequest,
	updateIUserRequest
} from '../model/userSlice';

const UserForm = () => {
	const { isLoading, errors, user } = useSelector(
		(state: RootState) => state.user
	);
	const dispatch = useDispatch();

	useEffect(() => {
		dispatch(getIUserRequest());
	}, [dispatch]);

	const [form, setForm] = useState({
		email: '',
		firstName: '',
		lastName: '',
		phone: ''
	});

	useEffect(() => {
		if (user) {
			// eslint-disable-next-line react-hooks/set-state-in-effect
			setForm({
				email: user.email || '',
				firstName: user.firstName || '',
				lastName: user.lastName || '',
				phone: user.phone || ''
			});
		}
	}, [user]);

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
		dispatch(updateIUserRequest(form));
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

			<Button loading={isLoading} type='submit' label='Продолжить' />
		</form>
	);
};

export default UserForm;
