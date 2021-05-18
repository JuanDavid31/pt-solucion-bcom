## Migraciones

### Crear DB

`CREATE USER "pt_user" WITH LOGIN SUPERUSER CREATEDB CONNECTION LIMIT -1 PASSWORD 'password'`

`CREATE DATABASE pt_dev`

`GRANT ALL PRIVILEGES ON DATABASE pt_dev TO pt_user`

### Crear un archivo de migración

1. En la carpeta `src/main/resources/db/changelog/migrations` crear un archivo donde el nombre tenga el número secuencial de la migración
y un nombre descriptivo EJ: `004-crear-tabla-xxx.sql`, `005-crear-indice-xxx-en-tabla-xxx.sql`, 
   `010-crear-procedimiento-almacenado-xxx.sql` etc.
   
2. El contenido de la migración debe empezar con las siguientes 2 lineas

```
--liquibase formatted sql
--changeset <nombre_del_autor>:<secuencial_de_la_migracion>
```

Donde el `<nombre_del_autor>` y `<secuencial_de_la_migracion>` deben ser reemplazados. Despues de estas 2 lineas debe ir el contenido
de la migración. Al finalizar la migración debe haber una o más lineas (si es necesario) que indiquean como deshacer la 
migración. EJ:

```
--rollback <sentencia_1>
--rollback <sentencia_2> 
```

### Correr todas las migraciones pendientes

`mvn liquibase:update`

### Como hacer rollback

`mvn liquibase:rolback -Dliquibase.rollbackCount=1`

donde el número indica la cantidad de changesets a los que se quiere hacer rollback.
