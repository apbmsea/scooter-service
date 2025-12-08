# Admin MFE

Микрофронтенд мониторинга запросов для scooter-service.

## Описание

Страница администрирования отображает список юзеров и возможность их удаления

## Технологии

- React 19
- TypeScript
- Redux Toolkit
- Typed Redux Saga
- SCSS
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
npx serve dist -p 3004 -C
```

## Структура

```
src/
├── app/
│   ├── store/store.ts  # Redux store
│   ├── App.tsx         # Точка входа
│   └── main.tsx        # Точка инициализации приложения
├── pages/
│   └── UsersPage/
│       ├── entities/    # Типы данных
│       ├── features/    # Логика и UI
│       │   ├── model/   # Redux slice и saga
│       │   └── ui/      # Компоненты
│       └── ui/          # Страница
└── shared/
    ├── hooks/           # Redux hooks
    └── types/           # Общие типы
```