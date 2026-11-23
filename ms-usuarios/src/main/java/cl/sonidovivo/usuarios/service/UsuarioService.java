package cl.sonidovivo.usuarios.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = repository.findAll();

        for (Usuario usuario : usuarios) {
            usuario.setClave(null);
        }

        return usuarios;
    }

    public Usuario buscar(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id: " + id));
        usuario.setClave(null);
        return usuario;
    }

    public Usuario crear(Usuario usuario) {
        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ese correo ya esta registrado");
        }

        usuario.setId(null);
        usuario.setClave(encoder.encode(usuario.getClave()));
        usuario.setActivo(true);

        Usuario guardado = repository.save(usuario);
        guardado.setClave(null);
        return guardado;
    }

    public Usuario editar(Long id, Usuario datos) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id: " + id));

        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setRol(datos.getRol());

        Usuario guardado = repository.save(usuario);
        guardado.setClave(null);
        return guardado;
    }

    public Usuario cambiarEstado(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id: " + id));

        usuario.setActivo(!usuario.getActivo());

        Usuario guardado = repository.save(usuario);
        guardado.setClave(null);
        return guardado;
    }

    public Usuario validarCredenciales(String correo, String clave) {
        Usuario usuario = repository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Correo o contrasena incorrectos"));

        if (!usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El usuario esta desactivado");
        }

        if (!encoder.matches(clave, usuario.getClave())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contrasena incorrectos");
        }

        return usuario;
    }
}
