# microservice-trabalho-final
Trabalho de Disciplina - Microservices &amp; Serveless Architecture

# Conta Corrente

Microserviço responsável pela orquestração da conta do cliente, com serviço de saldo, cartão de crédito e investimentos.

## Dependências utilizadas
- [Quarkus](https://code.quarkus.io/) com extensões:
--    YAML Configuration
--    RESTEasy JAX-RS
--    RESTEasy Jackson
--    SmallRye OpenAPI
--    Hibernate ORM with Panache
--    JDBC Driver - PostgreSQL
--    SmallRye Fault Tolerance
--    SmallRye OpenTracing
--    OpenID Connect
--    Keycloak Authorization


# Ambientes

É necessário subir um servidor de autenticação local na máquina para utilizar requisições usando JWT como recurso de segurança. Como opção foi selecionado o Banco de Dados PostGresSQL.

- Docker Image Keycloak: [Jboss/Keycloak](https://hub.docker.com/r/jboss/keycloak)
- Docker Postgres: [Postgres](https://hub.docker.com/_/postgres)

Rodar os containers: 
```
docker run -e "POSTGRES_PASSWORD=postgres" -p 5432:5432 -d --name postgres-db postgres:9.6.18-alpine 
docker run -d -p 10520:9091 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin jboss/keycloak
```

## Adicionando as tabelas no BD Postgres

Copie o arquivo `schemas.sql` para o container do postgres usando o comando abaixo.
```
docker cp ./schemas.sql postgres-db:/docker-entrypoint-initdb.d/schemas.sql
```

Depois execute o comando para criar o banco de dados e as tabelas.
```
docker exec -i postgres-db /bin/sh -c 'psql -U postgres -a -f docker-entrypoint-initdb.d/schemas.sql
```


## Keycloak para autenticação e acesso ao sistema bancário


Acessar o [Auth](http://localhost:10520/auth/) clicar em `Administration Console` e utilizar o usuário e senha `admin` e adicionar a configuração disponível no arquivo quarkus-realm.json na aba Quarkus > Add Realm fazendo o upload do arquivo.

Para testar o token adicionando usuário 'alice' com senha 'alice':

```
export access_token=$(\
    curl --insecure -X POST http://localhost:10520/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password&scope=openid' | jq --raw-output '.access_token' \
 )
```

Checando se o access_token retornou:

```
http :9091/api/users/me "Authorization: Bearer"$access_token
```
