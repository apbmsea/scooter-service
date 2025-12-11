# Header MFE

Микрофронтенд для хедера приложения Самокат

## Описание

Header MFE предоставляет адаптивную шапку с навигацией, которая меняется в зависимости от роли пользователя (USER, OPERATOR, ADMIN). Включает бургер-меню для мобильных устройств.

## Зависимости

host-client как среда инициализации проекта

auth-mfe для logout запросов

scooter-shared для управления запросами, типами, ивентами

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
│   ├── store/store.ts  # Точка входа в стор
│   ├── App.tsx         # Точка входа
│   └── main.tsx        # Точка инициализации приложения
├── entitites/
│   └── user/           # Сущность пользователя
│       ├── model/      # slice, saga, API
│       └── index.ts
└── widgets/
    └── Header/         # Компонент шапки
        ├── Header.tsx
        └── Header.scss
```

## API

### События (Необоходима инициализиация mfe host-client и auth-mfe)

Header подписывается на следующие события:

- `user_update` - обновление данных пользователя

Header генерирует следующие события:

- `logout` - выход пользователя

## Технологии

- React 19
- Redux Toolkit
- Typed Redux Saga
- TypeScript
- SCSS
- Vite

## Интеграция

Header использует `scooter-shared` библиотеку для:
- Навигации (`navigateTo`)
- Событий (`event.on`, `event.emit`)
- API запросов (`$api`)
- Типов (`User`)
