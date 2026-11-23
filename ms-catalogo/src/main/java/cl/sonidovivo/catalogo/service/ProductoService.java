package cl.sonidovivo.catalogo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.sonidovivo.catalogo.model.Producto;
import cl.sonidovivo.catalogo.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto guardar(Producto producto) {
        producto.setId(null);
        return repository.save(producto);
    }

    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = buscar(id);
        producto.setStock(cantidad);
        return repository.save(producto);
    }
}
