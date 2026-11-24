package cl.sonidovivo.catalogo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado con id: " + id));
    }

    public Producto guardar(Producto producto) {
        producto.setId(null);
        return repository.save(producto);
    }

    public Producto descontarStock(Long id, Integer cantidad) {
        Producto producto = buscar(id);

        int quedan = producto.getStock() - cantidad;

        if (quedan < 0) {
            quedan = 0;
        }

        producto.setStock(quedan);
        return repository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = buscar(id);
        repository.delete(producto);
    }

    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = buscar(id);
        producto.setStock(cantidad);
        return repository.save(producto);
    }
}
