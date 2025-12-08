# Auth MFE

Микрофронтенд аутентификации для scooter-service.

## Описание

Модуль аутентификации с формами входа, регистрации и восстановления пароля.

## Функционал

- Вход в систему (email/телефон + пароль)
- Регистрация нового пользователя
- Восстановление пароля
- Автоматическое обновление токена (refresh)
- Выход из системы
- Валидация форм с отображением ошибок
- Маска для ввода телефона
- Автоочистка ошибок при переходе между страницами

## Технологии

- React 19
- TypeScript
- Redux Toolkit
- Typed Redux Saga
- Consta UI Kit
- Maskito (маски ввода)
- Vite
- scooter-shared

## Установка

```bash
npm install
```
Локальная разработка mfe без инициализации проекта
```bash
npm run dev
```
Инициализация mfe для prod и итнеграции в проекта
```bash
npm run build
```
```bash
npx serve dist -p 3000 -C
```

## Структура

```
src/
├── app/
│   ├── store/           # Redux store, rootReducer, rootSaga
│   ├── App.tsx
│   └── main.tsx
├── entities/
│   ├── logout/          # API выхода
│   └── refresh/         # API обновления токена
├── features/
│   ├── logout/          # Логика выхода
│   └── refresh/         # Логика refresh токена
├── pages/
│   ├── LoginPage/       # Страница входа
│   │   ├── entities/    # Типы и API
│   │   ├── features/    # Форма входа
│   │   └── ui/          # Компонент страницы
│   ├── RegisterPage/    # Страница регистрации
│   └── RecoveryPage/    # Страница восстановления
└── shared/
    ├── hooks/           # Redux hooks
    ├── types/           # Общие типы
    └── utils/           # Утилиты (маски)
```

## Страницы

### LoginPage
- Email или телефон
- Пароль
- Ссылки на регистрацию и восстановление

### RegisterPage
- Имя
- Фамилия
- Email
- Телефон (с маской)
- Пароль
- Подтверждение пароля

### RecoveryPage
- Email для восстановления пароля

## Redux Store

### Slices
- `login` - состояние формы входа
- `register` - состояние формы регистрации
- `recovery` - состояние формы восстановления
- `logout` - состояние выхода
- `refresh` - состояние обновления токена

### Sagas
- `loginSaga` - обработка входа
- `registerSaga` - обработка регистрации
- `recoverySaga` - обработка восстановления
- `logoutSaga` - обработка выхода
- `refreshSaga` - обработка refresh токена

## События

Использует события из scooter-shared:
- `logout` - при выходе пользователя
- `refresh` - при обновлении токена
- `user_update` - при изменении данных пользователя

## API Endpoints

- `POST /auth/login` - вход
- `POST /auth/register` - регистрация
- `POST /auth/recovery` - восстановление пароля
- `POST /auth/logout` - выход
- `POST /auth/refresh` - обновление токена
