--liquibase formatted sql
--changeset JuanDavid:1
create table USUARIO
(
    id                 serial,
    nombre             VARCHAR(30),
    fecha_creacion     TIMESTAMP default now() not null,
    fecha_modificacion TIMESTAMP default now() not null,
    constraint USUARIO_PK primary key (ID)
);

--rollback drop table USUARIO