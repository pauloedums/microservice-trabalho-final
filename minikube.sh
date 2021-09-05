#Start do minikube
minikube stop

minikube start --memory=8192 --cpus=3

minikube status

minikube addons enable ingress

minikube status

minikube dashboard