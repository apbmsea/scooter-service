# scooter-shared

Общая библиотека для микрофронтендов scooter-service. Содержит утилиты, типы и инстансы для работы между микрофронтендами.

## Установка

```bash
npm install scooter-shared
```

## API

### Event Emitter

Типизированная система событий для коммуникации между микрофронтендами.

```typescript
import { event } from 'scooter-shared';

const unsubscribe = event.on('user_update', () => {
  console.log('User updated');
});

unsubscribe();

event.emit('logout');
event.emit('refresh');
event.emit('user_update');
```

**Доступные события:**
- `logout` - выход пользователя
- `refresh` - обновление токена
- `user_update` - обновление данных пользователя (сервисы могут подписаться и узнавть об апдейте юзера или информировать другие mfe)

### HTTP Instances

Общиее инстансы axios для API запросов, ловли и типизации ошибок.

```typescript
import { $api, $refresh } from 'scooter-shared';

const response = await $api.get('/users');

const tokens = await $refresh.post('/auth/refresh');
```

**Примечание:** Если инстансы не инициализированы (дев режим), используется обычный axios.

### Navigation

Утилита для навигации между роутами с поддержкой кастомного роутера.

```typescript
import { navigateTo } from 'scooter-shared';

navigateTo('/profile');
navigateTo('/scooters');
```

### Error Handling

Проверка и типизация обработанных ошибок.

```typescript
import { isHandledError, HandledError } from 'scooter-shared';

try {
  await $api.get('/data');
} catch (error) {
  if (isHandledError(error)) {
    console.log(error.data.message);
    console.log(error.data.errors);
  }
}
```

### Types

```typescript
import { User, HandledError, HandledErrorData } from 'scooter-shared';

interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  role: 'USER' | 'OPERATOR' | 'ADMIN';
  createdAt: string;
  updatedAt: string;
}

interface HandledError {
  status: number;
  data: HandledErrorData;
}

interface HandledErrorData {
  message: string;
  errors: Record<string, string>;
}
```

