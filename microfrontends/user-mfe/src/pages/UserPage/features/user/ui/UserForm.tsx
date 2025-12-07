import React, { useEffect, useState, useCallback, useMemo } from "react";
import { Button } from "@consta/uikit/Button";
import { TextField } from "@consta/uikit/TextField";
import {
  clearFieldError,
  getIUserRequest,
  updateIUserRequest,
} from "../model/userSlice";
import { useAppDispatch, useAppSelector } from "@shared/hooks/store.hooks";
import { event } from "@scooter/shared";
import "./UserForm.scss";

const initialFormState = {
  email: "",
  firstName: "",
  lastName: "",
  phone: "",
  password: "",
};

const UserForm = () => {
  const { user, isLoading, errors } = useAppSelector((state) => state);
  const dispatch = useAppDispatch();

  const [form, setForm] = useState(initialFormState);
  const [isPasswordEditing, setIsPasswordEditing] = useState(false);

  useEffect(() => {
    const fetchUser = () => dispatch(getIUserRequest());
    fetchUser();

    const unsubscribe = event.on("user_update", fetchUser);
    return unsubscribe;
  }, [dispatch]);

  useEffect(() => {
    if (!user) return;

    // eslint-disable-next-line react-hooks/set-state-in-effect
    setForm({
      email: user.email || "",
      firstName: user.firstName || "",
      lastName: user.lastName || "",
      phone: user.phone || "",
      password: "",
    });

    setIsPasswordEditing(false);
  }, [user]);

  const handleChange = useCallback(
    (field: keyof typeof form) => (value: string | null) => {
      const newValue = value ?? "";
      setForm((prev) => ({ ...prev, [field]: newValue }));

      if (errors[field]) {
        dispatch(clearFieldError(field));
      }
    },
    [errors, dispatch]
  );

  const handlePasswordEdit = useCallback(() => {
    setIsPasswordEditing(true);
    setForm((prev) => ({ ...prev, password: "" }));
  }, []);

  const handleSubmit = useCallback(
    (e: React.FormEvent) => {
      e.preventDefault();

      const { password, ...restForm } = form;

      const payload = {
        ...restForm,
        ...(isPasswordEditing && password.trim() ? { password } : {}),
      };

      dispatch(updateIUserRequest(payload));
    },
    [form, isPasswordEditing, dispatch]
  );

  const passwordPlaceholder = useMemo(
    () => (isPasswordEditing ? "Введите новый пароль" : "••••••••"),
    [isPasswordEditing]
  );

  return (
    <form className="user-form" onSubmit={handleSubmit}>
      <TextField
        label="Почта"
        placeholder="example@gmail.com"
        withClearButton
        value={form.email}
        status={errors.email ? "alert" : undefined}
        caption={errors.email || ""}
        onChange={handleChange("email")}
        disabled={isLoading}
      />

      <TextField
        label="Имя"
        placeholder="Иван"
        withClearButton
        value={form.firstName}
        status={errors.firstName ? "alert" : undefined}
        caption={errors.firstName || ""}
        onChange={handleChange("firstName")}
        disabled={isLoading}
      />

      <TextField
        label="Фамилия"
        placeholder="Иванов"
        withClearButton
        value={form.lastName}
        status={errors.lastName ? "alert" : undefined}
        caption={errors.lastName || ""}
        onChange={handleChange("lastName")}
        disabled={isLoading}
      />

      <TextField
        label="Телефон"
        placeholder="Введите номер"
        withClearButton
        value={form.phone}
        status={errors.phone ? "alert" : undefined}
        caption={errors.phone || ""}
        onChange={handleChange("phone")}
        disabled={isLoading}
      />

      <div className="user-form__password">
        <TextField
          label="Пароль"
          placeholder={passwordPlaceholder}
          type="password"
          withClearButton={isPasswordEditing}
          value={form.password}
          status={errors.password ? "alert" : undefined}
          caption={errors.password || ""}
          onChange={handleChange("password")}
          disabled={!isPasswordEditing || isLoading}
        />

        {!isPasswordEditing && (
          <Button
            type="button"
            label="Изменить пароль"
            view="ghost"
            size="s"
            onClick={handlePasswordEdit}
            disabled={isLoading}
          />
        )}
      </div>

      <Button loading={isLoading} type="submit" label="Сохранить" />
    </form>
  );
};

export default UserForm;
