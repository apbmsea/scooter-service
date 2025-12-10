# Scooter Service

Микросервисная архитектура для управления сервисом аренды самокатов с микрофронтенд подходом.

## Инфа

Данные для админа:
email: weewee@admin.com
password: 123123qq

если пароль не совпадает - сбростье через забыл пароль

host: http://10.3.19.162:5000
gateway: https://10.4.3.81:8080

## Архитектура проекта

Проект построен на микросервисной архитектуре с микрофронтендами:

```
scooter-service/
├── host-client/          # Хост для микрофронтендов
├── microfrontends/       # Микрофронтенды
│   ├── header-mfe/          # Шапка сайта
│   ├── home-mfe/            # Главная страница
│   ├── auth-mfe/            # Авторизация
│   ├── user-mfe/            # Профиль пользователя
│   ├── admin-mfe/           # Админ панель
│   └── monitoring-mfe/      # Мониторинг
├── services/             # Микросервисы
└── shared/              # Общие библиотеки
    └── scooter-shared/      # Общая библиотека
```

## Компоненты системы

### 🌐 [Host Client](./host-client/README.md)
Центральный хост-сервер для управления микрофронтендами:
- Маршрутизация между MFE
- Контроль доступа и авторизация
- Проксирование ресурсов
- SSL соединение

**Технологии:** TypeScript, Express.js, HTTPS

### 🎨 Микрофронтенды

#### [Header MFE](./microfrontends/header-mfe/README.md)
Компонент шапки сайта с навигацией и профилем пользователя.

#### [Home MFE](./microfrontends/home-mfe/README.md)
Главная страница с информацией о сервисе.

#### [Auth MFE](./microfrontends/auth-mfe/README.md)
Система авторизации: вход, регистрация, восстановление пароля.

#### [User MFE](./microfrontends/user-mfe/README.md)
Профиль пользователя и настройки аккаунта.

#### [Admin MFE](./microfrontends/admin-mfe/README.md)
Административная панель для управления пользователями.

#### [Monitoring MFE](./microfrontends/monitoring-mfe/README.md)
Мониторинг системы и просмотр логов запросов.

**Технологии MFE:** React, TypeScript, Webpack Module Federation, SCSS

**Технологии сервисов:** Node.js, Express.js, MongoDB/PostgreSQL

### 📚 [Shared Library](./shared/scooter-shared/README.md)
Общая библиотека с переиспользуемыми компонентами:
- Общие типы и интерфейсы
- Утилиты и хелперы
- Константы
- Общие компоненты React

**Технологии:** TypeScript, React

## Быстрый старт

### Предварительные требования
- Node.js
- npm
- npx

### Установка

1. Клонирование репозитория:
```bash
git clone https://github.com/apbmsea/scooter-service.git
cd scooter-service
```

2. Установка зависимостей для всех проектов:
```bash

Производить запуск в разных терминалах

# Установка host-client
cd ./host-client && npm install && npm run dev

# Установка микрофронтендов
cd ./microfrontends/header-mfe && npm install && npm run build && npx serve dist -p 3000 -C
cd ./microfrontends/home-mfe && npm install && npm run build && npx serve dist -p 3001 -C
cd ./microfrontends/auth-mfe && npm install && npm run build && npx serve dist -p 3002 -C
cd ./microfrontends/user-mfe && npm install && npm run build && npx serve dist -p 3003 -C
cd ./microfrontends/admin-mfe && npm install && npm run build && npx serve dist -p 3004 -C
cd ./microfrontends/monitoring-mfe && npm install && npm run build && npx serve dist -p 3005 -C
```

3. Настройка переменных в env 🌐 [Host Client](./host-client/README.md)


Приложение будет доступно по адресу: `https://вашайписетиколледжа:5000`

## Архитектурные принципы

### Микрофронтенды
- **Независимость**: каждый MFE может разрабатываться и деплоиться независимо
- **Технологическая свобода**: разные MFE могут использовать разные технологии
- **Изоляция**: изоляция стилей и состояния между MFE

### Безопасность
- HTTPS соединения
- JWT токены для аутентификации
- Ролевая модель доступа
- CORS настройки

## Мониторинг и логирование

- Централизованное логирование через Monitoring Service
- Метрики производительности
- Отслеживание ошибок
- Health checks для всех сервисов
