# Client Microservice REST API

### Стек

- **Платформа**: Java, Spring (Boot, Data JPA);
- **Хранение данных**: PostgreSQL, Hibernate ORM, FlyWay;
- **Деплой**: Maven, Docker, Docker Compose;
- **Дополнительные технологии**: Lombok, Jackson, Postman;

### Endpoints

- `GET: /api/v1/clients` - Возвращает список клиентов в формате JSON.
<br><br>
- `GET: /api/v1/clients/{id}` - Возвращает клиента с заданным id. Возвращает объект клиента с соответствующим ID.
<br><br>
- `POST: /api/v1/clients/add` - Принимает данные клиента в формате JSON в теле запроса. Если сохранение прошло успешно, возвращает объект клиента.
<br><br>
- `PUT: /api/v1/clients/{id}/update` - Принимает ID клиента в качестве параметра пути и данные клиента в формате JSON в теле запроса. Возвращает обновленный объект клиента.
<br><br>
- `DELETE /api/v1/clients/{id}/delete` - Принимает ID клиента в качестве параметра пути. Удаляет клиента и возвращает строку с информацией об удалении с соответствующим ID.