Como hacer una migración

`mvn liquibase:update`

Como hacer rollback

`mvn liquibase:rolback -Dliquibase.rollbackCount=1` 
donde el número indica la cantidad de changesets a los que se quiere hacer rollback.

Crear DB

`CREATE USER "pt_user" WITH LOGIN SUPERUSER CREATEDB CONNECTION LIMIT -1 PASSWORD 'password'`

`CREATE DATABASE pt_dev`

`GRANT ALL PRIVILEGES ON DATABASE pt_dev TO pt_user`