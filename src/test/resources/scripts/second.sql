INSERT INTO usuario(nombre) values (''), ('Juan'), ('David');
INSERT INTO evento(nombre, fecha, id_creador) values ('Dummy 1', now(), 2), ('Dummy 1', now(), 2);
INSERT INTO asistencia(id_usuario, id_evento) values (3, 2);