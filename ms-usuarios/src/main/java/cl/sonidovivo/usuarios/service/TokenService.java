package cl.sonidovivo.usuarios.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cl.sonidovivo.usuarios.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private final SecretKey clave;
    private final long duracion;

    public TokenService(@Value("${app.jwt.secreto}") String secreto,
                        @Value("${app.jwt.duracion}") long duracion) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.duracion = duracion;
    }

    public String generar(Usuario usuario) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + duracion);

        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol())
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(clave)
                .compact();
    }
}
