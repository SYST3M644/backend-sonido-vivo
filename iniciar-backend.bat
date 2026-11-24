@echo off
REM Levanta todo el backend de Sonido Vivo, cada servicio en su propia ventana.
REM Antes de usarlo, MySQL tiene que estar encendido.

cd /d %~dp0

echo Iniciando Eureka...
start "Eureka 8761" cmd /k "cd eurekaserver && mvnw.cmd spring-boot:run"
timeout /t 25 /nobreak

echo Iniciando API Gateway...
start "Gateway 6464" cmd /k "cd apigateway && mvnw.cmd spring-boot:run"
timeout /t 10 /nobreak

echo Iniciando microservicios...
start "ms-usuarios 8081" cmd /k "cd ms-usuarios && mvnw.cmd spring-boot:run"
start "ms-contacto 8082" cmd /k "cd ms-contacto && mvnw.cmd spring-boot:run"
start "ms-catalogo 8083" cmd /k "cd ms-catalogo && mvnw.cmd spring-boot:run"
start "ms-pedidos 8084" cmd /k "cd ms-pedidos && mvnw.cmd spring-boot:run"

echo.
echo Listo. Revisa http://localhost:8761 en uno o dos minutos: deben aparecer los servicios.
pause
