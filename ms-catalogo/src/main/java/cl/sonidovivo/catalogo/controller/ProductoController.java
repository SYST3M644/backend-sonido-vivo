package cl.sonidovivo.catalogo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sonidovivo.catalogo.model.Producto;
import cl.sonidovivo.catalogo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Productos", description = "Catálogo de instrumentos y su stock")
@RequestMapping("api/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar productos", description = "Devuelve todos los productos del catálogo. Es pública: no necesita token.")
    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @Operation(summary = "Buscar un producto", description = "Devuelve un producto por su id. Es pública.")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @Operation(summary = "Crear producto", description = "Agrega un producto nuevo al catálogo. Solo vendedor o administrador.")
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Producto guardado = service.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Descontar stock", description = "Resta del stock las unidades compradas. Se usa al confirmar un pedido.")
    @PutMapping("/{id}/descontar")
    public ResponseEntity<Producto> descontarStock(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = service.descontarStock(id, producto.getStock());
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Actualizar stock", description = "Cambia las unidades disponibles de un producto. Solo vendedor o administrador.")
    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = service.actualizarStock(id, producto.getStock());
        return ResponseEntity.ok(actualizado);
    }
}
