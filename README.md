# Sistema bancário - Microservices & Serverless Architecture Impacta
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

# Criação das imagens 

Executar o script bash para criar as imagens do docker do projeto.
Primeiro é preciso liberar acesso no linux para sua execução com o comando `chmod u+x docker-images.sh`.

Depois é preciso executar o arquivo bash `./docker-images.sh`. Este executável irá criar as imagens dos microserviços no docker para serem utilizadas na sequência com o uso do docker-compose(a confirmar).

# Ambientes

É necessário subir um servidor de autenticação local na máquina para utilizar requisições usando JWT como recurso de segurança. Como opção foi selecionado o Banco de Dados PostGresSQL.

- Docker Image Keycloak: [Jboss/Keycloak](https://hub.docker.com/r/jboss/keycloak)
- Docker Postgres: [Postgres](https://hub.docker.com/_/postgres)

Primeiro é preciso liberar acesso no linux para sua execução com o comando `chmod u+x docker-containers.sh`.

Rode os containers `./docker-containers.sh`.

Utilize o arquivo tesouro-direto.json para adicionar investimentos no endpoint `http://localhost:9020/investments`.


## Keycloak para autenticação e acesso ao sistema bancário


Acessar o [Auth](http://localhost:10520/auth/) clicar em `Administration Console` e utilizar o usuário e senha `admin` e adicionar a configuração disponível no arquivo quarkus-realm.json na aba Quarkus > Add Realm fazendo o upload do arquivo.

Para testar o token adicionando usuário 'user1' com senha 'user1':

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
