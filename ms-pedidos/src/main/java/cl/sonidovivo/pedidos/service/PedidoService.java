package cl.sonidovivo.pedidos.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.sonidovivo.pedidos.model.Pedido;
import cl.sonidovivo.pedidos.model.PedidoItem;
import cl.sonidovivo.pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    private static final List<String> ESTADOS = List.of("en preparacion", "despachado", "entregado");

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listar() {
        return repository.findAll();
    }

    public Pedido buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado con id: " + id));
    }

    public List<Pedido> listarDeCliente(String cliente) {
        return repository.findByCliente(cliente);
    }

    public List<Pedido> listarDelMes(String mes) {
        if (mes == null || "todos".equals(mes)) {
            return repository.findAll();
        }

        YearMonth periodo = YearMonth.parse(mes);
        LocalDate desde = periodo.atDay(1);
        LocalDate hasta = periodo.atEndOfMonth();

        return repository.findByFechaBetween(desde, hasta);
    }

    public Pedido crear(Pedido pedido) {
        pedido.setId(null);
        pedido.setEstado("en preparacion");

        for (PedidoItem item : pedido.getItems()) {
            item.setId(null);
            item.setPedido(pedido);
        }

        return repository.save(pedido);
    }

    public Pedido cambiarEstado(Long id, String estado) {
        if (!ESTADOS.contains(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no valido: " + estado);
        }

        Pedido pedido = buscar(id);
        pedido.setEstado(estado);
        return repository.save(pedido);
    }
}
