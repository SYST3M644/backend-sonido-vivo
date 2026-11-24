package cl.sonidovivo.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Usuarios", description = "Administración de cuentas")
@RequestMapping("api/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar usuarios", description = "Devuelve todas las cuentas, sin la contraseña. Solo administrador.")
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @Operation(summary = "Buscar un usuario", description = "Devuelve una cuenta por su id. Solo administrador.")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @Operation(summary = "Crear usuario", description = "Crea una cuenta con cualquier rol. Solo administrador.")
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario creado = service.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Editar usuario", description = "Cambia el nombre, correo y rol de una cuenta. Solo administrador.")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.editar(id, usuario));
    }

    @Operation(summary = "Activar o desactivar", description = "Bloquea o desbloquea una cuenta sin borrarla. Solo administrador.")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(service.cambiarEstado(id));
    }

    @Operation(summary = "Eliminar usuario", description = "Borra una cuenta definitivamente. Solo administrador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
