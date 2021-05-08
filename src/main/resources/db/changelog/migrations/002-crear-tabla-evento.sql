--liquibase formatted sql
--changeset JuanDavid:2
create table EVENTO
(
    id                 serial,
    nombre             varchar(30)                         not null,
    descripcion        text,
    fecha_modificacion timestamp default now() not null,
    fecha_creacion     timestamp default now() not null,
    fecha              timestamp                            not null,
    id_creador         int,
    constraint EVENTO_PK primary key (id),
    constraint EVENTO_FK foreign key (id_creador) references USUARIO
);

--rollback drop table EVENTO