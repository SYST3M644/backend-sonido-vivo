package cl.sonidovivo.contacto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.contacto.model.Mensajeria;
import cl.sonidovivo.contacto.service.MensajeriaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name = "Contacto", description = "Mensajes del formulario de contacto") 
@RequestMapping("api/contactos")
public class MensajeriaController {
    private final MensajeriaService service;

    private MensajeriaController(MensajeriaService service){
        this.service = service;
    }

    @Operation(summary = "Enviar mensaje", description = "Guarda un mensaje del formulario de contacto. Es pública.")
    @PostMapping
    public ResponseEntity<Mensajeria> guardar(@RequestBody Mensajeria contacto) {
        Mensajeria guardado = service.guardar(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Listar mensajes", description = "Devuelve todos los mensajes recibidos. Solo administrador.")
    @GetMapping
    public List<Mensajeria> listar() {
        return service.listar();
    }

    @Operation(summary = "Marcar como recibido", description = "Cambia el estado del mensaje a RECIBIDO. Solo administrador.")
    @PutMapping("/{id}")
    public ResponseEntity<Mensajeria> actualizarEstado(@PathVariable Long id) {
        Mensajeria guardado = service.actualizarEstado(id);
        return ResponseEntity.ok(guardado);
    }

    
    
}
