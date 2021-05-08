Como hacer una migración

`mvn liquibase:update`

Como hacer rollback

`mvn liquibase:rolback -Dliquibase.rollbackCount=1` 
donde el número indica la cantidad de changesets a los que se quiere hacer rollback.