package cl.sonidovivo.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.usuarios.model.Usuario;
import cl.sonidovivo.usuarios.service.UsuarioService;

@RestController
@RequestMapping("api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario creado = service.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.editar(id, usuario));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(service.cambiarEstado(id));
    }
}
