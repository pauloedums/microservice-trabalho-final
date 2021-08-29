docker network create -d bridge microservices  
docker run --rm=true --net=microservices -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=bank_accounts -p 5432:5432 -d --name postgres-db postgres:9.6.18-alpine
docker run --net=microservices -d -p 10520:8080 viniciusmartinez/quarkus-rhsso:1.0
docker run --net=microservices -d -p 10020:10020 --name ms-impacta-credit microservices-impacta/credit:latest 
docker run --net=microservices -d -p 10030:10030 --name ms-impacta-debit microservices-impacta/debit:latest
docker run --net=microservices -d -p 9010:9010 --name ms-impacta-credit-card microservices-impacta/credit-card:latest
docker run --net=microservices -d -p 9000:9000 --name ms-impacta-extract-balance microservices-impacta/extract-balance-api:latest
docker run --net=microservices -it -p 9020:9020 --name ms-impacta-investments microservices-impacta/investments:latest