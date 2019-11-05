drop table persona if exists;

create table persona(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    nombre VARCHAR(20),
    apellido VARCHAR (20),
    dni VARCHAR(10));