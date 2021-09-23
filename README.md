# Sistema bancário - Microservices & Serverless Architecture Impacta
Trabalho de Disciplina - Microservices &amp; Serveless Architecture

# Conta Corrente

Microserviço responsável pela orquestração da conta do cliente, com serviço de saldo, cartão de crédito e investimentos.

## Pré-requisitos
- [Ubuntu](https://ubuntu.com/) Linux OS - versão 20.04 
- [minikube](https://minikube.sigs.k8s.io/docs/start/) versão 1.22.0 

## Dependências utilizadas no [Quarkus](https://code.quarkus.io/):
- YAML Configuration
- RESTEasy JAX-RS
- RESTEasy Jackson
- SmallRye OpenAPI
- Hibernate ORM with Panache
- JDBC Driver - PostgreSQL
- SmallRye Fault Tolerance
- SmallRye OpenTracing
- SmallRye Metrics
- SmallRye Health
- OpenID Connect
- Keycloak Authorization

## Microserviços

0. [Ambientes](#microservicos-ambientes)
1. [Recursos Kubernetes](#microservices-kubernetes)
2. [Adição de hostnames para teste local](#microservices-hostnames)
3. [Keycloak para autenticação e acesso ao sistema bancário](#microservices-keycloak)
4. [Utilização de métricas e health check](#microservices-health-check)
5. [Utilização do Swagger](#microservices-swagger)
6. [Excluir o namespace e todas as dependências](#microservices-delete-namespace)
7. [Parar o minikube](#microservices-stop-minikube)

## Implementação

### <a name="microservicos-ambientes">0 - Ambientes</a>

O projeto utiliza o servidor de autenticação local no Kubernetes para utilizar requisições usando JWT como recurso de segurança. Como opção foi selecionado o Banco de Dados PostGresSQL.

- Image Keycloak: [Jboss/Keycloak](https://hub.docker.com/r/viniciusmartinez/quarkus-rhsso)
- Postgres: [Postgres](https://hub.docker.com/_/postgres)

Primeiro é preciso liberar acesso no linux para sua execução com o comando `chmod u+x minikube.sh`.

Após isto execute o shell script `./minikube.sh`.

Este comando irá iniciar o minikube e instalar o ingress-controller para ser utilizado dentro do Kubernetes, com o comando `minikube addons enable ingress` e no final irá abrir o dashboard do kubernetes.


### <a name="microservices-kubernetes">1 - Recursos Kubernetes</a>

É necessário liberar acesso no linux para sua execução com o comando `chmod u+x kubectl-microservices.sh`.

Após isto execute o shell script `./kubectl-microservices.sh`.

Este comando irá criar o namespace `microservicos-impacta` e todas as suas dependências de serviços, deployments, stateful para o BD e recursos ingress.


### <a name="microservices-hostnames">2 - Adição de hostnames para teste local</a>

Por não estarmos utilizando a nuvem pública é necessário adicionar a configuração no arquivo hosts da máquina.

### importante!

Copie o seu **IP ADDRESS** utilizado, cheque os ips abaixo rodando o comando `kubectl get ingress -n microservices-impacta`.

```
NAME                         CLASS    HOSTS                                                                                                                    ADDRESS          PORTS   AGE
ms-impacta-account-ingress   <none>   microservices-impacta-account.com                                                                                        192.168.99.105   80      22h
ms-impacta-debit-ingress     <none>   microservices-impacta-debit.com,microservices-impacta-credit.com,microservices-impacta-extract-balance.com + 3 more...   192.168.99.105   80      22h
```

Utilizando o ambiente linux atualize o arquivo `sudo vim /etc/hosts` com os endereços abaixo.

```
<SEU-IP>    microservices-impacta-debit.com
<SEU-IP>    microservices-impacta-credit.com
<SEU-IP>    microservices-impacta-extract-balance.com
<SEU-IP>    microservices-impacta-investments.com
<SEU-IP>    microservices-impacta-credit-card.com
<SEU-IP>    microservices-impacta-account.com
<SEU-IP>    microservices-impacta-keycloak.com
```

### <a name="microservices-keycloak">3 - Keycloak para autenticação e acesso ao sistema bancário</a>

Acessar o [Auth](http://microservices-impacta-keycloak.com/auth/) clicar em `Administration Console` e utilizar o usuário e senha `admin` e adicionar a configuração adicional no menu `Clients > customer-app`.

Trocar as urls de **Root URL, Valid Redirect URIs, Admin URL e Web Origins** para o valor `http://microservices-impacta-account.com/`.

## Utilização

### <a name="microservices-health-check">4 - Utilização de métricas e health check</a>

Métricas `http://microservices-impacta-account.com/q/app_metrics`.

Health Check  `http://microservices-impacta-account.com/check`.


### <a name="microservices-swagger">5 - Utilização do Swagger</a>

Acesse a URL `http://microservices-impacta-account.com/q/swagger-ui/#/`.
Clique em Authorize, onde tem um ícone de cadeado e adicione os seguintes dados.

```
username: admin
password: admin
client_id: customer-app
client_secret: 5ffb3490-4d7b-42ed-8cac-e6774550bc92
```

### Adicionando um cliente:
Recurso POST
​/admin​/client​/add
```
{
  "accountNumber": 102035,
  "cpf": 123456789,
  "firstName": "Teste",
  "lastName": "02"
}

```

### Adicionando Tesouro Direto
Recurso POST
/admin/tesouro-direto

Utilizar o arquivo `tesouro-direto.json`.


### Adicionando um cartão de crédito para o cliente
Recurso POST
/admin/{cpf}/add-credit-card

```
CPF: 123456789
{
  "cardName": "Meu cartão",
  "cardNumber": 1020564789
}
```


### Adicionando um investimento para o cliente
Recurso PUT
​/investments​/{cpf}​/add-investment

```
CPF: 123456789
{
  "codeTesouroDireto": 159,
  "investmentValue": 10,
  "quantidade": 1
}
```

### Recurso para checar os valores
POST
​/client​/{cpf}

```
CPF: 123456789

```

Resposta:
```
Teste, seja bem-vindo ao Banco X. 
Seu saldo atual é de R$ $4,489.01
------------------------- 

Investimentos: 
------------------------- 
Nome: Tesouro Selic 2023
Data de encerramento: 01/03/2023
Valor mínimo de investimento: $1.00
Valor investido: $10.00
------------------------- 
Cartão de Crédito: 
------------------------- 
Nome do cartão: Meu cartão
Número do cartão de crédito: 1020564789
Valor de limite do cartão: $2,500.00
Valor atual do cartão de crédito: $499.99
Nome do cliente: Teste João
------------------------- 

```

### <a name="microservices-delete-namespace">6 - Excluir o namespace e todas as dependências</a>

Rode o comando `kubectl delete ns microservices-impacta`.


### <a name="microservices-stop-minikube">7 - Parar o minikube</a>

Rode o comando `minikube stop`.
