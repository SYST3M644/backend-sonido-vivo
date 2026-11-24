package cl.sonidovivo.pedidos.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.pedidos.model.Pedido;
import cl.sonidovivo.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Pedidos", description = "Pedidos de la tienda")
@RequestMapping("api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar pedidos", description = "Sin filtros devuelve todos (vendedor o administrador). Con ?cliente= devuelve los de ese cliente y con ?mes=AAAA-MM los de ese mes.")
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

    @Operation(summary = "Buscar un pedido", description = "Devuelve un pedido con sus productos.")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @Operation(summary = "Crear pedido", description = "Registra una compra. Es pública para que también compren los invitados.")
    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        Pedido creado = service.crear(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Cambiar estado", description = "Avanza el pedido: en preparacion, despachado o entregado. Solo vendedor o administrador.")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(service.cambiarEstado(id, pedido.getEstado()));
    }

    @Operation(summary = "Eliminar pedido", description = "Borra un pedido junto con sus productos. Solo administrador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
