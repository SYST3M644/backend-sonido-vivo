package cl.sonidovivo.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class FiltroJwt implements GlobalFilter, Ordered {

    private static final String PREFIJO = "Bearer ";

    private static final List<String> LIBRES = List.of(
            "/api/auth/login"
    );

    private static final String CONTACTO = "/api/contactos";
    private static final String PEDIDOS = "/api/pedidos";

    private final SecretKey clave;

    public FiltroJwt(@Value("${app.jwt.secreto}") String secreto) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange intercambio, GatewayFilterChain cadena) {
        ServerHttpRequest peticion = intercambio.getRequest();

        if (libre(peticion)) {
            return cadena.filter(intercambio);
        }

        String cabecera = peticion.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (cabecera == null || !cabecera.startsWith(PREFIJO)) {
            return rechazar(intercambio);
        }

        Claims datos;

        try {
            datos = Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(cabecera.substring(PREFIJO.length()))
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return rechazar(intercambio);
        }

        // El token es valido: ahora se revisa si su rol puede usar esta ruta
        String rol = datos.get("rol", String.class);
        String nombre = datos.get("nombre", String.class);

        if (!permitido(peticion, rol, nombre)) {
            return prohibir(intercambio);
        }

        return cadena.filter(intercambio);
    }

    // Reglas de acceso por rol (cliente, vendedor, administrador)
    private boolean permitido(ServerHttpRequest peticion, String rol, String nombre) {
        String ruta = peticion.getURI().getPath();
        HttpMethod metodo = peticion.getMethod();

        boolean esAdministrador = "administrador".equals(rol);
        boolean esVendedor = "vendedor".equals(rol);
        boolean atiendeLaTienda = esAdministrador || esVendedor;

        // Usuarios y mensajes de contacto: solo el administrador
        if (ruta.startsWith("/api/usuarios") || ruta.startsWith(CONTACTO)) {
            return esAdministrador;
        }

        // Productos: crear y cambiar stock lo hace la tienda, borrar solo el administrador
        if (ruta.startsWith("/api/productos")) {
            if (HttpMethod.DELETE.equals(metodo)) {
                return esAdministrador;
            }

            return atiendeLaTienda;
        }

        if (ruta.startsWith(PEDIDOS)) {
            if (HttpMethod.DELETE.equals(metodo)) {
                return esAdministrador;
            }

            if (HttpMethod.PUT.equals(metodo)) {
                return atiendeLaTienda;
            }

            // Un cliente solo puede ver sus propios pedidos
            String cliente = peticion.getQueryParams().getFirst("cliente");

            if (cliente != null) {
                return atiendeLaTienda || cliente.equals(nombre);
            }

            // La lista completa de pedidos es solo para la tienda
            if (ruta.equals(PEDIDOS)) {
                return atiendeLaTienda;
            }

            return true;
        }

        // Cualquier usuario con sesion: por ejemplo renovar su token
        return true;
    }

    private boolean libre(ServerHttpRequest peticion) {
        String ruta = peticion.getURI().getPath();
        HttpMethod metodo = peticion.getMethod();

        if (HttpMethod.OPTIONS.equals(metodo)) {
            return true;
        }

        if (LIBRES.stream().anyMatch(ruta::startsWith)) {
            return true;
        }

        if (HttpMethod.POST.equals(metodo) && CONTACTO.equals(ruta)) {
            return true;
        }

        if (HttpMethod.POST.equals(metodo) && PEDIDOS.equals(ruta)) {
            return true;
        }

        // Al confirmar una compra se descuenta el stock, y el invitado no tiene token
        if (HttpMethod.PUT.equals(metodo) && ruta.startsWith("/api/productos/") && ruta.endsWith("/descontar")) {
            return true;
        }

        return HttpMethod.GET.equals(metodo) && ruta.startsWith("/api/productos");
    }

    // 403: el token es valido pero el rol no tiene permiso
    private Mono<Void> prohibir(ServerWebExchange intercambio) {
        intercambio.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return intercambio.getResponse().setComplete();
    }

    // 401: no hay token o no es valido
    private Mono<Void> rechazar(ServerWebExchange intercambio) {
        intercambio.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return intercambio.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
