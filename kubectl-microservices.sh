#Start do minikube
minikube start --memory=8192 --cpus=3
minikube status

# Adicionar deploys para o kubernetes
kubectl apply -k kubernetes/

#Checar se está tudo configurado e rodando
kubectl get all -n microservices-impacta

# rodar este comando para conseguir pegar o ip para fazer a conexão com o BD
minikube -n microservices-impacta service postgres-db-lb --url

# comando para checar o ingress do namespace
kubectl get ingress -n microservices-impacta