# Job Analyser

**«Job Analyser – анализ рынка труда»** представляет собой REST API микросервисное приложение для проведения аналитики открытых вакансий, предложений конкурентов
и изменений трендов на рынке труда.

**Целью создания веб-приложения** является сокращение временных, кадровых и финансовых ресурсов, затрачиваемых на поиск и привлечение кандидатов.

## Навигация

- [Функциональное назначение](#functions).
- [Использованные технологии](#tools).
- [Архитектурные решения](#architecture).
- [Запуск и настройка приложения](#starting).
- [Endpoints](#endoints).


<a name="functions"></a>
## Функциональное назначение

Основные функциональные требования:
1. **Агрегация данных**: приложение должно собирать данные о вакансиях, предложениях конкурентов и изменениях на рынке труда из различных открытых источников.
2. **Аналитика вакансий**: приложение должно проводить анализ открытых вакансий (средняя заработная плата, тенденции компетенций и квалификаций и прочее).
Аналитика должна позволять определить тренды на рынке труда, востребованные навыки и изменения в требованиях к кандидатам.
3. **Анализ компаний-конкурентов**: приложение должно проводить анализ предложений конкурентов, включая информацию о названии вакансии, описании, требованиях к заработной плате и компетенциях и других характеристиках.
Анализ должен помочь определить конкурентные преимущества и слабые места организации.
4. **Мониторинг трендов**: приложение должно отслеживать изменения на рынке труда, такие как изменение требований к кандидатам, повышение или снижение средней заработной платы, популярных навыков и других факторов. Мониторинг должен позволить организации быть в курсе последних тенденций и принимать соответствующие решения.

<a name="tools"></a>
## Использованные технологии

- **Платформа**: Java, Spring Boot;
- **Библиотеки**: Lombok, Log4j, Gson;
- **Взаимодействие с БД**: Hibernate ORM, Spring Data JPA, PostgreSQL;
- **Кеширование**: Redis;
- **Сборка и развертывание**: Maven, Docker, Docker-Compose, Apache Tomcat;

<a name="architecture"></a>
## Архитектурные решения

**Приложение состоит из следующих микро сервисов:**
- сервис-парсер данных с крупнейших агрегаторов поиска работы;
- сервис для анализа собранной статистики;
- сервис для взаимодействия с СУБД и базой данных;
- сервис для конфигурации вышеописанных модулей и обработки ошибок.

**В ходе разработки применены следующие шаблоны проектирования:**
- Model View Control;
- Data transfer Object;
- Repository;
- Singleton;
- Dependency Injection Pattern;
- Object Relative Mapping.

**По структуре классы можно разделить на следующие слои:**
1. **Слой модели, пакет «entity»** – отображают сущности базы данных.
2. **Слой контроллеров, пакет «controller»** – точки входа в программу, принимают и обрабатывают входящие HTTP-запросы,
вызывая методы классов из слоя-сервиса. Контроллеры формируют объекты слоя-представления и возвращают их вместе с HTTP-кодом завершения операции.
3. **Слой представления, пакет «dto»** – объекты в удобном для представления для пользователя виде.
4. **Слой-сервис, пакет «service»** – интерфейсы и их реализации, содержащие методы бизнес-логики для работы с соответствующими моделями.
5. **Слой репозиториев, пакет «repository»** – классы для обращения с запросами к соответствующим сущностям в базе данных.
6. **Слой конфигурации, пакет «config»** – конфигурационные классы.
7. **Пакет «exception»** содержит классы-обработчики одноименных исключений.
8. **Пакет «utils»** – вспомогательный классы.
9. **Пакет «mapper»** – классы для серилизации и десериализации моделей в/из json.

<a name="starting"></a>
## Запуск и настройка приложения

Технические средства, необходимые **для запуска**: JDK, Maven, Docker.

### 1. Конфигурация и запуск контейнеров
1. Файл конфигурации контейнеров расположен в пути `docker/docker-compose.yml`. При необоходимости изменить конфигурацию подключения к БД и порты:

postgreSQL
```dockerfile
environment:
  POSTGRES_USER: postgres
  POSTGRES_PASSWORD: postgres
  POSTGRES_DB: postgres
ports:
  - "5432:5432"
```

redis
```dockerfile
ports:
  - "6379:6379"
```
2. Запуск контейнеров
 ```shell
cd .\docker\
docker-compose up
```

### 2. Конфигурация и запуск Java-сервисов

1. Настройка подключения к БД:
```
# Spring properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vacancy_analyser
spring.datasource.username=postgres
spring.datasource.password=postgres

# redis
spring.data.redis.port=6379
spring.data.redis.host=localhost
```
При необходимости порты сервисов можно изменить в свойстве
```
server.port=
```

2. Запуск сервиса-парсера (по умолчанию порт 8085).
3. Запуск сервиса для анализа собранной статистики (по умолчанию порт 8086).

<a name="endpoints"></a>
## Endpoints

## data-parser
**Порт**: 8085.

HTTP GET: `/api/v1/data/refresh` - собирает данные о работодателях и их открытых вакансиях.

| Параметр     | Обязятелен | Описание                                                                                                                  |
|--------------|------------|---------------------------------------------------------------------------------------------------------------------------|
| vacancy_name | false      | Собрать данные только о тех работодателях, у которых есть вакансия <vacancy_name>                                         |
| location     | false      | Собрать данные о работодателях в определенном городе/стране/районе <location>                                             |
| pages_amount | false      | Установить количество страниц с данными для загрузки (1 страница ~ 20 позиций), по умолчанию загружаться будут все данные |

Пример запроса:
`:8085/api/v1/data/refresh?location=казань&pages_amount=1`

Пример ответа:
```json
{
  "status": "success",
  "description": "Previous data deleted, new data initialisation completed, sorted by location(казань)",
  "time_spent": "2501ms",
  "filtered_by_vacancy_name": null,
  "filtered_by_location": "казань",
  "pages_amount": 1,
  "date_time": "2023-07-15T01:55:58.4046538"
}
```

Пример запроса:
`:8085/api/v1/data/refresh?vacancy_name=java+разрабочик&pages_amount=1`

Пример ответа:
```json
{
  "status": "success",
  "description": "Previous data deleted, new data initialisation completed, sorted by vacancy_name(java разрабочик)",
  "time_spent": "3284ms",
  "filtered_by_vacancy_name": "java разрабочик",
  "filtered_by_location": null,
  "pages_amount": 1,
  "date_time": "2023-07-15T01:58:37.6700729"
}
```

Пример ответа с ошибкой:
```json
{
    "status": "error",
    "time_spent": null,
    "filtered_by_vacancy_name": null,
    "filtered_by_location": "фцвфцвфцв",
    "date_time": "2023-07-15T02:04:20.5552602",
    "description": "Http request sending error, message: Location not found. Response status: 404. Evoked from DataInitializerImpl (fillVacanciesForEmployer)",
    "path": "/api/v1/data/refresh"
}
```

