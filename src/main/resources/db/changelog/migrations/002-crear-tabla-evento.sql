--liquibase formatted sql
--changeset JuanDavid:1
create table EVENTO
(
    id                 int auto_increment,
    nombre             varchar2(30)                         not null,
    descripcion        text,
    fecha_modificacion datetime default current_timestamp() not null,
    fecha_creacion     datetime default current_timestamp() not null,
    fecha              datetime                             not null,
    id_creador         int,
    constraint EVENTO_PK primary key (id),
    constraint EVENTO_FK foreign key (id_creador) references USUARIO
);

--rollback drop table EVENTO