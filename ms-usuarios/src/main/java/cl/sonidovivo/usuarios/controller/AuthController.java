package cl.sonidovivo.usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.usuarios.dto.LoginRequest;
import cl.sonidovivo.usuarios.dto.LoginResponse;
import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.service.TokenService;
import cl.sonidovivo.usuarios.service.UsuarioService;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public AuthController(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

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
