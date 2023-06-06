# hh.ru parser



## Использованные технологии

- **Платформа**: Java, Spring Boot;
- **Библиотеки**: Lombok, Log4j, Gson;
- **Взаимодействие с БД**: Hibernate ORM, Spring Data JPA, PostgreSQL;
- **Кеширование**: Redis;
- **Сборка и развертывание**: Maven, Docker, Docker-Compose, Apache Tomcat;

<br>

## Endpoints

<h4 align="center">Работодатели</h4>

- `GET: /api/v1/employers` - возвращает список работодателей;
<br><br>
- `GET: /api/v1/employers?vacancy=<название вакансии>` - возвращает список работодателей, у которых
есть вакансия с определенным названием 
<br>(Пример: _/api/v1/employers?vacancy=java+разработчик_);
<br><br>
- `GET: /api/v1/employers?vacancy=<название вакансии>&location=<регион поиска>` - возвращает список работодателей
  у которых есть вакансия с определенным названием в определенном регионе;
<br>(Пример: _/api/v1/employers?vacancy=java+разработчик&location=москва_);

Пример ответа:
```json
{
    "found": 20,
    "items": [
      {
        "id": 5775464,
        "name": "000 Mediva",
        "location": "Ташкент",
        "url": "https://hh.ru/employer/5775464",
        "description": null,
        "openVacancies": 0
      },
      . . .
      {
        "id": 3643187,
        "name": "001KZ (001КЗ)",
        "location": "Алматы",
        "url": "https://hh.ru/employer/3643187",
        "description": "Интернет-магазин 1.kz Наши категории: компьтеная техника, смартфоны, бытовая техника и электроника, сантехника, товары для дома и сада, автотовары и шины, товары для туризма,  игрушки и конструкторы, зоотовары, парфюмерия и многое другое.",
        "openVacancies": 5
      }
    ]
}
```