--liquibase formatted sql
--changeset JuanDavid:1
create table USUARIO
(
    ID                 INT auto_increment,
    NOMBRE             VARCHAR(30),
    FECHA_CREACION     TIMESTAMP default current_timestamp() not null,
    FECHA_MODIFICACION TIMESTAMP default current_timestamp() not null,
    constraint USUARIO_PK primary key (ID)
);

--rollback drop table USUARIO