package cl.sonidovivo.pedidos.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.pedidos.model.Pedido;
import cl.sonidovivo.pedidos.service.PedidoService;

@RestController
@RequestMapping("api/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) String cliente,
                               @RequestParam(required = false) String mes) {
        if (cliente != null) {
            return service.listarDeCliente(cliente);
        }

        if (mes != null) {
            return service.listarDelMes(mes);
        }

        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        Pedido creado = service.crear(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(service.cambiarEstado(id, pedido.getEstado()));
    }
}
