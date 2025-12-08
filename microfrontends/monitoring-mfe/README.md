# Monitoring MFE

Микрофронтенд мониторинга запросов для scooter-service.

## Описание

Страница мониторинга отображает список запросов в реальном времени с информацией о методе, статусе, сервисе, сообщении и времени выполнения.

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
npx serve dist -p 3000 -C
```

## Структура

```
src/
├── app/
│   ├── store/store.ts  # Redux store
│   ├── App.tsx         # Точка входа
│   └── main.tsx        # Точка инициализации приложения
├── pages/
│   └── RequestsPage/
│       ├── entities/    # Типы данных
│       ├── features/    # Логика и UI
│       │   ├── model/   # Redux slice и saga
│       │   └── ui/      # Компоненты
│       └── ui/          # Страница
└── shared/
    ├── hooks/           # Redux hooks
    └── types/           # Общие типы
```

## Типы запросов

- **SUCCESS** - успешные запросы (зеленый)
- **WARNING** - предупреждения (оранжевый)
- **ERROR** - ошибки (красный)

## Колонки таблицы

1. Метод (GET, POST, PUT, DELETE)
2. Статус (HTTP код)
3. Сервис (название сервиса)
4. Сообщение (описание запроса)
5. Время (дата и время выполнения)
6. Тип (SUCCESS/WARNING/ERROR)

## API

Получает данные с эндпоинта `/api/requests` каждые 5 секунд.
