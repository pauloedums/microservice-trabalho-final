

#Criar o namespace
kubectl create namespace microservices-impacta

# crie um deploy da imagem do postgres
kubectl -n microservices-impacta create deployment postgres-db --image=postgres:9.6.18-alpine

#Adicionar a configuração do postgres
kubectl -n microservices-impacta apply -f postgres/postgres-db-config.yml

#Adicionar o postgres
kubectl -n microservices-impacta apply -f postgres/postgres.yml

#Subir o serviço do postgres
kubectl -n microservices-impacta apply -f postgres/postgres-db-service.yml

#Checar se está tudo configurado e rodando
kubectl get all

# rodar este comando para conseguir pegar o ip para fazer a conexão com o BD
minikube -d -n microservices-impacta service postgres-db-lb

# Pegar o IP exposto no serviço 'service/postgres-db-lb' e 
# testar a conexão com o Banco de Dados atráves de outro software: Dbeaver ou pgAdmin