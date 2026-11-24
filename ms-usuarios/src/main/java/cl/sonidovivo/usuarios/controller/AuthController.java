package cl.sonidovivo.usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.usuarios.dto.LoginRequest;
import cl.sonidovivo.usuarios.dto.LoginResponse;
import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.service.TokenService;
import cl.sonidovivo.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Autenticación", description = "Login y tokens JWT")
@RequestMapping("api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public AuthController(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Iniciar sesión", description = "Valida correo y contraseña y devuelve los datos del usuario con su token JWT (dura 1 hora).")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest peticion) {
        Usuario usuario = usuarioService.validarCredenciales(peticion.getCorreo(), peticion.getClave());

        String token = tokenService.generar(usuario);

        LoginResponse respuesta = new LoginResponse(
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol(),
                token);

        return ResponseEntity.ok(respuesta);
    }
}
