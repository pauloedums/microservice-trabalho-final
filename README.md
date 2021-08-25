# microservice-trabalho-final
Trabalho de Disciplina - Microservices &amp; Serveless Architecture

# Conta Corrente

Microserviço responsável pela orquestração da conta do cliente, com serviço de saldo, cartão de crédito e investimentos.

## Dependências utilizadas
 [Quarkus](https://code.quarkus.io/) com extensões:
-    YAML Configuration
-    RESTEasy JAX-RS
-    RESTEasy Jackson
-   SmallRye OpenAPI
-    Hibernate ORM with Panache
-    JDBC Driver - PostgreSQL
-    SmallRye Fault Tolerance
-    SmallRye OpenTracing
-    OpenID Connect
-    Keycloak Authorization


# Ambientes

É necessário subir um servidor de autenticação local na máquina para utilizar requisições usando JWT como recurso de segurança. Como opção foi selecionado o Banco de Dados PostGresSQL.

- Docker Image Keycloak: [Jboss/Keycloak](https://hub.docker.com/r/jboss/keycloak)
- Docker Postgres: [Postgres](https://hub.docker.com/_/postgres)

Rodar os containers: 
```
docker run -e "POSTGRES_PASSWORD=postgres" -p 5432:5432 -d --name postgres-db postgres:9.6.18-alpine 
docker run -p 10520:8080 viniciusmartinez/quarkus-rhsso:1.0
```

## Adicionando as tabelas no BD Postgres

Copie o arquivo `schemas.sql` para o container do postgres usando o comando abaixo.
```
docker cp ./schemas.sql postgres-db:/docker-entrypoint-initdb.d/schemas.sql
```

Depois execute o comando para criar o banco de dados e as tabelas.
```
docker exec -i postgres-db bash -c 'psql -U postgres -a -f docker-entrypoint-initdb.d/schemas.sql'
```


## Keycloak para autenticação e acesso ao sistema bancário


Acessar o [Auth](http://localhost:10520/auth/) clicar em `Administration Console` e utilizar o usuário e senha `admin` e adicionar a configuração disponível no arquivo quarkus-realm.json na aba Quarkus > Add Realm fazendo o upload do arquivo.

Para testar o token adicionando usuário 'alice' com senha 'alice':

Usando usuário cliente comum:
```
export access_token=$(\
  curl -X POST http://localhost:10520/auth/realms/Quarkus/protocol/openid-connect/token \
  --user customer-app:5ffb3490-4d7b-42ed-8cac-e6774550bc92 \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d 'username=user1&password=user1&grant_type=password' | jq --raw-output '.access_token' \
)
echo $access_token
```

Usando usuário admin comum:
```
export access_token=$(\
  curl -X POST http://localhost:10520/auth/realms/Quarkus/protocol/openid-connect/token \
  --user customer-app:5ffb3490-4d7b-42ed-8cac-e6774550bc92 \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d 'username=admin&password=admin&grant_type=password' | jq --raw-output '.access_token' \
)
echo $access_token
```

Checando se o access_token retornou:

```
curl -X 'GET' \
  'http://localhost:8080/api/clients/me' \
  -H 'accept: text/plain' \
  -H 'Authorization: Bearer '$access_token
```

Listar todos os clientes:
```
curl -X 'GET' \
  'http://localhost:8080/api/admin/clients' \
  -H 'accept: text/plain' \
  -H 'Authorization: Bearer '$access_token
```
