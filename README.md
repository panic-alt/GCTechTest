# GCTechTest
Prueba técnica para Global Commerce

# Servicios de Suscripción

## Descripción

Este proyecto es una aplicación Spring Boot 3.3.2 para gestionar un servicio de suscripción a noticias. 

## Tecnologías Utilizadas

### Spring Boot

- **Spring Boot Starter Web**: Proporciona la base para construir aplicaciones web y servicios RESTful con Spring MVC. Incluye soporte para servidor embebido Tomcat y auto-configuración para tareas comunes de desarrollo web.
- **Spring Boot Starter Data JPA**: Simplifica el acceso a bases de datos utilizando Spring Data JPA y Hibernate, ofreciendo una capa de abstracción sobre bases de datos relacionales.
- **Spring Boot Starter Security**: Proporciona características de seguridad integrales, incluyendo autenticación y autorización, utilizando Spring Security.
- **Spring Boot Starter JDBC**: Soporta JDBC (Java Database Connectivity) para operaciones con bases de datos e integra con la gestión de transacciones de Spring.
- **Spring Boot DevTools**: Proporciona características para el desarrollo como reinicios automáticos y recargas en vivo para mejorar la experiencia de desarrollo.
- **Springdoc OpenAPI Starter WebMVC UI**: Genera automáticamente documentación de la API usando OpenAPI y proporciona una interfaz de usuario web para interactuar con la API.

### H2 Database

- **H2**: Una base de datos en memoria utilizada para desarrollo y pruebas. Ofrece un motor de base de datos liviano que se puede incrustar fácilmente en la aplicación.
- La base de datos aloja tres tablas.
- USERS: aloja un usuario admin que sirve para tener acceso a la aplicación sin la necesidad de llevar a cabo un registro. User: admin, password: password, phoneNumber: +123123123.
- NEWS_CATEGORIES: aloja una lista predefinida de categorías de noticias a las cuales el usuario se puede suscribir, estas son: Sports, Politics, Entertainment, Weather, Science y Business.
- SUBSCRIPTIONS: aloja las suscripciones hechas por los usuarios.

### JSON Web Token (JWT)

- **jjwt-api**, **jjwt-impl**, **jjwt-jackson**: Bibliotecas para crear y analizar JSON Web Tokens (JWT). Los JWT se utilizan para autenticación y autorización basada en tokens de manera segura.

### Mockito

- **Mockito**: Un popular marco de simulación para pruebas unitarias en Java. Permite crear objetos simulados y definir su comportamiento para fines de prueba.

### JUnit

- **JUnit**: Un marco de pruebas ampliamente utilizado para Java que proporciona anotaciones y aserciones para escribir y ejecutar pruebas unitarias.

## Thymeleaf

- **Thymeleaf**: un motor de plantillas en el servidor moderno para renderizar páginas web. También se implemento Bootstrap.


## Empezar

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/panic-alt/GCTechTest.git

## URLs 
1. Documentación de APIs del backend:
- http://localhost:8080/swagger-ui/index.html

2. Vista de inicio de sesión: 
- http://localhost:8080/api/login (User: admin, password: password para pruebas).

3. Vista de registro de usuarios:
- http://localhost:8080/api/register

4. Vista home page (es necesario iniciar sesión o registrarse para operar):
- http://localhost:8080/api/home 

5. Base de datos H2 (User: sa, Password: password):
- http://localhost:8080/h2-console/ 
