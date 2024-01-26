# microservices
Implemetaciòn de microservicios con Spring Boot, complementando con el uso de Spring Cloud Gateway, Eureka netflix, kafka, WebFlux y bases de datos como MySql y Postgres.

ENDPOINTS:
Como primer paso se debe crear un usuario usando el metodo registry, el cual se utilizará al momento de la generación del token.

1.	http://localhost:8080/registry - Endpoint inicial para la creación de usuarios.
2.	http://localhost:8080/api/login - Endpoint para la generación de token

Con el token generado ya se puede consumir los demás microservicios, a traves del api-gateway, enviando el token en el Autorization

3.	http://localhost:8080/api/product  - Api para la creaciòn de productos
4.	http://localhost:8080/api/order - Api para la creaciòn de ordenes
5.	http://localhost:8080/api/inventory/{sku} - Api para la validaciòn de stock de productos por sku en el inventario.


El proyeto cuenta con un total de 7 microservicios:
1. Api-gateway: Microservicio para la implementaciòn del gateway que da acceso a los demás microservicios usando Spring Cloud Gateway y Eureka. El gateway expone por el puerto 8080.
2. Autenticate-service: Microservicio implementado con Spring Security y uso de JWT para la autenticación e identificación de los usuarios que contaran con un JWT para poder consumir los microservicios.
3. Discovery-server: Microservicio que aloja el servidor de Eureka con la finalidad de localizar y registrar servicios con los que se desea interactuar, además nos ayuda con el balanceo de carga y la tolerancia a fallos.
   
   <img width="960" alt="eureka2" src="https://github.com/jennifer-haya04/microservices/assets/93691562/0c621b7d-ba7e-43af-8d54-ebdf3b301015">

5. Iventory-service: Microservicio relacionado con la tabla productos, que permite consultar el stock de los productos de manera indepediente haciendo uso de una base de datos Postgres
6. Notification-service: Microservicio utilizado por kafka para el tratamieto de eventos y topics.
7. Orders-service: Microservicio utilizado al momento de la creaciòn de una orden de productos, las ordenes son almacenadas en una base de datos MySQL
8. Products-service: Microservicio para el tratamiento de productos, con base de datos MySQL.

*****+Consideraciones de la Prueba Tecnica*******
1. Normalizacion de Tablas: Para este punto considere oportuno el rediseño de las tablas Usuario y Rol para mejor manejo de los datos de la siguiente manera:
   
  ![Captura de pantalla 2024-01-26 a la(s) 11 15 39](https://github.com/jennifer-haya04/microservices/assets/93691562/77d5ddcf-9adc-417e-89eb-badcfeb1ad9b)
  
  Todos los microservicios cuenta con una estructura similar, teniedo un model, repository, service y controller y otros segun las caracterisiticas del microservicio:
  
  <img width="358" alt="Captura de pantalla 2024-01-26 a la(s) 11 17 42" src="https://github.com/jennifer-haya04/microservices/assets/93691562/8c1571a7-29c9-4305-924a-064147c0e0f2">
  
3. Autenticacion con JWT: Este microservicio cuenta con tres metodos principales en el controller que son registry, validateUser y validateToken. El metodo validateUser recibe en el request el username y el password que el usuario utilizará para la creación del JWT:

  ![Captura de pantalla 2024-01-26 a la(s) 11 22 52](https://github.com/jennifer-haya04/microservices/assets/93691562/7df7f320-47d3-447b-a32d-13d60441b514)
  
4. Programación reactiva: Se hizo uso de WebFlux para la comunicaciòn entre los microservicios Orders-service y inventory-service, puesto que como parte del flujo implementado al momento de crear una orden antes se debe validar el stock en el área de inventario de la siguiente forma:
5. 
  <img width="934" alt="Captura de pantalla 2024-01-26 a la(s) 11 26 34" src="https://github.com/jennifer-haya04/microservices/assets/93691562/70e4e5bb-1305-41c7-9a7d-20dc878dd9a7">
  
4.Dockerización: Para este punto se agrego el archivo DockerFile en el microservicio products-service, se hace la construcción de la imagen con docker build -t image rutaDockerFile:

  <img width="480" alt="Captura de pantalla 2024-01-26 a la(s) 11 29 12" src="https://github.com/jennifer-haya04/microservices/assets/93691562/6a8a5f35-afda-4a0c-b0c9-0fc64246e63e">
  
  Una vez lista la imagen, esta se subio a mi repository de dockerHub https://hub.docker.com/repository/docker/jenniferhaya/microservices
  
  ![Captura de pantalla 2024-01-26 a la(s) 11 31 01](https://github.com/jennifer-haya04/microservices/assets/93691562/88540ac3-b90f-4696-8d19-266675bd6b7a)
  
5.Kafka: Se realizó la implementación de un topico de kafka para la comunicación del evento al momento de la ceaciòn de la orden( Se observa tambien en el punto 3)
