drop table if exists clients cascade;
create table clients (
                         id bigserial not null,
                         email varchar(255) not null,
                         first_name varchar(50) not null,
                         last_name varchar(255) not null,
                         primary key (id)
)