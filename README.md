## Spring Boot Microservice Sample Project

Project consists of 4 modules; eureka server, cloud gateway, product and order services

All nested projects are dockerized and the whole project can be run via single docker compose command

### Product service has:

api/products/** endpoints for crud operations

api/products/order/** endpoints to increment or decrement stock quantity by order create or cancel requests

### Order service has:

api/orders/** endpoints to create, complete or cancel orders

Order service provides access to the product service using a feign client that uses as default ribbon to make loadbalanced requests

---

To run the project:

* clone the repository, open the project root folder in your terminal

* run 'docker compose up -d' command

* default ports are:
  - 8000: eureka-server
  - 8001: cloud-gateway
  - 8002: product-service
  - 8003: order-service

* open [eureka server management page](http://localhost:8000) on your web browser to see your services
* for the service endpoint documentations you can look:
  - [product service swagger url](http://localhost:8002/swagger-ui.html)
  - [order service swagger url](http://localhost:8003/swagger-ui.html)

For cleanup run the commands below:
```
docker stop sample-order-service
docker stop sample-product-service
docker stop sample-eureka-server
docker stop sample-cloud-gateway

docker rm sample-cloud-gateway
docker rm sample-eureka-server
docker rm sample-product-service
docker rm sample-order-service

docker rmi sample-cloud-gateway
docker rmi sample-eureka-server
docker rmi sample-product-service
docker rmi sample-order-service
```
