package cl.sonidovivo.apigateway.config;

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

        try {
            Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(cabecera.substring(PREFIJO.length()));
        } catch (JwtException | IllegalArgumentException e) {
            return rechazar(intercambio);
        }

        return cadena.filter(intercambio);
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

    private Mono<Void> rechazar(ServerWebExchange intercambio) {
        intercambio.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return intercambio.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
