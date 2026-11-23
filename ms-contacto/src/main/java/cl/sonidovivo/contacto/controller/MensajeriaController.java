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



@RestController 
@RequestMapping("api/contactos")
public class MensajeriaController {
    private final MensajeriaService service;

    private MensajeriaController(MensajeriaService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Mensajeria> guardar(@RequestBody Mensajeria contacto) {
        Mensajeria guardado = service.guardar(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    public List<Mensajeria> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mensajeria> actualizarEstado(@PathVariable Long id) {
        Mensajeria guardado = service.actualizarEstado(id);
        return ResponseEntity.ok(guardado);
    }

    
    
}
