# Sonido Vivo - Backend

Backend de la tienda Sonido Vivo, hecho con microservicios Spring Boot.

Proyecto del caso semestral de DSY1104 - Desarrollo FullStack II.

El frontend esta en:
https://github.com/SYST3M644/SONIDO_VIVO_PROJECTO

## Tecnologias

- Java 21 y Spring Boot
- Spring Cloud Gateway y Eureka
- MySQL con Flyway (cada microservicio tiene su propia base de datos)
- JWT para la autenticacion y BCrypt para las contraseñas
- Swagger (springdoc) para documentar las APIs

## Servicios

| Servicio | Puerto | Que hace | Swagger |
|---|---|---|---|
| eurekaserver | 8761 | Registro de los microservicios | - |
| apigateway | 6464 | Punto de entrada unico, valida el token y el rol | - |
| ms-usuarios | 8081 | Login, registro, renovar token y usuarios | http://localhost:8081/swagger-ui.html |
| ms-contacto | 8082 | Mensajes del formulario de contacto | http://localhost:8082/swagger-ui.html |
| ms-catalogo | 8083 | Productos, stock e imagenes | http://localhost:8083/swagger-ui.html |
| ms-pedidos | 8084 | Pedidos y su seguimiento | http://localhost:8084/swagger-ui.html |

El frontend solo habla con el API Gateway (http://localhost:6464/api/...).

## Seguridad

- El login devuelve un token JWT que dura 1 hora
- El API Gateway revisa el token en cada peticion: sin token responde 401 y si el rol no tiene permiso responde 403
- Rutas libres: login, registro, ver productos, enviar un mensaje de contacto y crear un pedido (compra como invitado)

| Rol | Puede |
|---|---|
| Cliente | Comprar y ver solo sus propios pedidos |
| Vendedor | Ver y cambiar el estado de los pedidos, crear productos y actualizar el stock |
| Administrador | Todo, mas usuarios, mensajes, reportes y eliminar registros |

## Usuarios de prueba

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | admin@sonidovivo.cl | admin123 |
| Vendedor | vendedor@sonidovivo.cl | vende123 |
| Cliente | cliente@sonidovivo.cl | cliente123 |

## Como ejecutar

1. Encender MySQL en localhost:3306 con el usuario root sin contraseña. Las bases de datos y tablas se crean solas con Flyway.
2. Hacer doble clic en `iniciar-backend.bat`. Abre una ventana por servicio en este orden: Eureka, Gateway y los 4 microservicios.
3. Revisar http://localhost:8761: deben aparecer USUARIOS, CONTACTO, CATALOGO, PEDIDOS y APIGATEWAY.

Tambien se puede levantar cada servicio a mano, entrando a su carpeta:

```
mvnw.cmd spring-boot:run
```
