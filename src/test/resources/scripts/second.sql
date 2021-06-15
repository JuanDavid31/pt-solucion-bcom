INSERT INTO usuario(id, nombre) values (1, ''), (2, 'Juan'), (3, 'David');
INSERT INTO evento(id, nombre, fecha, id_creador) values (100, 'Dummy 1', now(), 2), (200, 'Dummy 1', now(), 2);
INSERT INTO asistencia(id_usuario, id_evento) values (2, 200);
INSERT INTO asistencia(id_usuario, id_evento) values (3, 200);