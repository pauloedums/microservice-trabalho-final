# Deletar namespace se houver
kubectl delete ns microservices-impacta

# Adicionar deploys para o kubernetes
kubectl apply -k kubernetes/

#Checar se está tudo configurado e rodando
kubectl get all -n microservices-impacta

echo '\n';
echo 'Use este ip para se conectar ao banco de dados:';
# rodar este comando para conseguir pegar o ip para fazer a conexão com o BD
minikube -n microservices-impacta service postgres-db-lb --url
echo '\n';