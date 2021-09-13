# Sistema bancário - Microservices & Serverless Architecture Impacta
Trabalho de Disciplina - Microservices &amp; Serveless Architecture

# Conta Corrente

Microserviço responsável pela orquestração da conta do cliente, com serviço de saldo, cartão de crédito e investimentos.

## Dependências utilizadas
 [minikube](https://minikube.sigs.k8s.io/docs/start/)
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


## Ambientes

É necessário subir um servidor de autenticação local no Kubernetes para utilizar requisições usando JWT como recurso de segurança. Como opção foi selecionado o Banco de Dados PostGresSQL.

- Image Keycloak: [Jboss/Keycloak](https://hub.docker.com/r/jboss/keycloak)
- Postgres: [Postgres](https://hub.docker.com/_/postgres)

Primeiro é preciso liberar acesso no linux para sua execução com o comando `chmod u+x kubectl-microservices.sh`.

Após isto execute o shell script `./kubectl-microservices.sh`.

Este comando irá criar o namespace `microservicos-impacta` e todas as suas dependências.


## Adição de hostnames para teste local

Por não estarmos utilizando a nuvem pública é necessário adicionar a configuração no arquivo hosts da máquina.

Mas antes precisamos habilitar o ingress dentro do namespace utilizado.
Para isto rode o comando `minikube addons enable ingress`.


### importante!

Cheque se o seu ip utilizado corresponde com os ips abaixo, para isto rode o comando `kubectl get ingress -n microservices-impacta`.

Utilizando o ambiente linux atualize o arquivo `sudo vim /etc/hosts` com os endereços abaixo.

```
192.168.49.2    microservices-impacta-debit.com
192.168.49.2    microservices-impacta-credit.com
192.168.49.2    microservices-impacta-extract-balance.com
192.168.49.2    microservices-impacta-investments.com
192.168.49.2    microservices-impacta-credit-card.com
192.168.49.2    microservices-impacta-account.com
192.168.49.2    microservices-impacta-keycloak.com
```


## Carga de dados no microserviço de investimentos

Utilize o arquivo tesouro-direto.json para adicionar investimentos no endpoint `http://microservices-impacta-investments.com/investments`.


## Keycloak para autenticação e acesso ao sistema bancário


Acessar o [Auth](http://microservices-impacta-keycloak.com/auth/) clicar em `Administration Console` e utilizar o usuário e senha `admin` e adicionar a configuração disponível no arquivo realm-export.json na aba Quarkus > Add Realm fazendo o upload do arquivo.


## Excluir o namespace e todas as dependências

Primeiro é preciso liberar acesso no linux para sua execução com o comando `chmod u+x kubectl-delete-microservices.sh`.
Rode o shell script `./kubectl-delete-microservices.sh`.