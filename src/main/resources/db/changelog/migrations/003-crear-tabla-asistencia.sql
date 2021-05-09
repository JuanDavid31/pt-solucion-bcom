--liquibase formatted sql
--changeset JuanDavid:3
create table ASISTENCIA
(
    id_usuario int not null constraint asistencia_usuario_fk references usuario on delete cascade,
    id_evento  int not null constraint asistencia_evento_fk references evento on delete cascade
);

--rollback drop table ASISTENCIA