package cl.sonidovivo.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.service.UsuarioService;
import cl.sonidovivo.usuarios.repository.UsuarioRepository;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final UsuarioService service;

    public DatosIniciales(UsuarioRepository repository, UsuarioService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void run(String... args) {
        crear("Carlos Rojas", "admin@sonidovivo.cl", "admin123", "administrador");
        crear("Ana Soto", "vendedor@sonidovivo.cl", "vende123", "vendedor");
        crear("Pedro Diaz", "cliente@sonidovivo.cl", "cliente123", "cliente");
    }

    private void crear(String nombre, String correo, String clave, String rol) {
        if (repository.existsByCorreo(correo)) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setClave(clave);
        usuario.setRol(rol);

        service.crear(usuario);
    }
}
